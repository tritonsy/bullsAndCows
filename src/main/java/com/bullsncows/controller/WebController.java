package com.bullsncows.controller;

import com.bullsncows.entity.Attempt;
import com.bullsncows.entity.Game;
import com.bullsncows.repository.AttemptsRepository;
import com.bullsncows.repository.GamesRepository;
import com.bullsncows.repository.PlayersRepository;
import com.bullsncows.service.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class WebController {

    @Autowired
    PlayersRepository playersRepository;
    @Autowired
    GamesRepository gamesRepository;
    @Autowired
    AttemptsRepository attemptsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    Service service;

    @Bean
    Validator getRegistrationValidator() {
        return new Validator() {
            @Override
            public boolean supports(Class<?> aClass) {
                return RegistrationForm.class.equals(aClass);
            }

            @Override
            public void validate(Object o, Errors errors) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "", "Couldn't be empty");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "", "Couldn't be empty");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "", "Couldn't be empty");

                if (errors.hasErrors()) return;

                final RegistrationForm form = (RegistrationForm) o;
                if (!StandardCharsets.US_ASCII.newEncoder().canEncode(form.getLogin()))
                    errors.rejectValue("login", "", "Can't read not ascii symbols.");

                if (errors.hasErrors()) return;

                if (playersRepository.getByLogin(form.getLogin()).isPresent())
                    errors.rejectValue("login", "", "Nickname already exists");

                if (!form.getPassword().equals(form.getPasswordConfirm())) {
                    errors.rejectValue("password", "", "Different passwords");
                    errors.rejectValue("passwordConfirm", "", "Different passwords");
                }
            }
        };
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @GetMapping(value = {"/", "/login"})
    String login(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "login";
    }

    @PostMapping(value = "/registration")
    String registration(
            @ModelAttribute("registrationForm")
                    RegistrationForm form, BindingResult bindingResult, Model model) {
        getRegistrationValidator().validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            //To show registration window
            model.addAttribute("regError", true);
            return "login";
        }
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        service.save(form);
        model.addAttribute("registrationSuccess", "Registration success");
        return "login";
    }

    @GetMapping(value = "/person/{login}")
    String personPage(
            @PathVariable
                    String login, Model model) throws IllegalArgumentException {
        model.addAttribute("user",
                service.loadPlayer(login, true)
                        .orElseThrow(() -> new IllegalArgumentException("This player doesn't exists")));
        //For dates formatting in jsp
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return "playerPage";
    }

    /**
     * Finds game by id. If it doesn't exist in db or isn't in not completed list of user, method generates new game.
     *
     * @param id of game
     * @return founded game or new
     * @throws IllegalArgumentException if incorrect login of player. handled by @ErrorHandler
     */
    @GetMapping(value = "/game/{id}")
    String gameById(
            @PathVariable
                    Integer id, Model model) throws IllegalArgumentException {
        final String login = getUsername();
        final LinkedList<Game>
                notEndedGamesOfPrincipal =
                new LinkedList<>(gamesRepository.getNotEndedGamesByPlayerLogin(login));
        Game game;
        final Optional<Game> optionalGame = gamesRepository.findById(id).filter(notEndedGamesOfPrincipal::contains);
        if (optionalGame.isPresent()) {
            game = optionalGame.get();
            notEndedGamesOfPrincipal.remove(game);
        } else {
            game = service.newGame(login);
            game.setAttempts(Collections.emptyList());
            model.addAttribute("error", "Error while finding specified game");
        }
        model.addAttribute("game", game);
        model.addAttribute("list", notEndedGamesOfPrincipal);
        return "game";
    }

    /**
     * Continues last not completed game of player or generated new game, if this not completed games list is empty
     *
     * @return last not completed game of player or new game
     * @throws IllegalArgumentException if incorrect login of player. handled by @ErrorHandler
     */
    @GetMapping(value = "/game")
    String game(Model model) throws IllegalArgumentException {
        final String login = getUsername();
        final LinkedList<Game>
                notEndedGamesOfPrincipal =
                new LinkedList<>(gamesRepository.getNotEndedGamesByPlayerLogin(login));
        final Optional<Game> optionalGame = Optional.ofNullable(notEndedGamesOfPrincipal.pollFirst());
        Game game = optionalGame.orElseGet(() -> service.newGame(login));
        model.addAttribute("game", game);
        model.addAttribute("list", notEndedGamesOfPrincipal);
        return "game";
    }

    @GetMapping(value = "/newGame")
    String newGame(Model model) throws IllegalArgumentException {
        final String login = getUsername();
        model.addAttribute("list", gamesRepository.getNotEndedGamesByPlayerLogin(login));
        Game game = service.newGame(login);
        model.addAttribute("game", game);
        return "game";
    }

    private String getUsername() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    /**
     * Responses on ajax request to add new attempt in game. Checks constraints on correct attempted number, correct
     * game and player.
     *
     * @return response string on not completed attempt, string 'Victory!' on game over or error text on any error
     */
    @PostMapping(value = "/newAttempt", consumes = "text/plain")
    @ResponseBody
    ResponseEntity<?> newAttempt(
            @RequestBody
                    String body) throws IOException {
        try {
            final Map<String, String>
                    requestBody =
                    objectMapper().readValue(body, new TypeReference<Map<String, String>>() {
                    });
            final String login = getUsername();
            final Integer gameId = Integer.parseInt(requestBody.get("gameId"));
            final String attemptNumber = requestBody.get("attempt");
            if (!attemptNumber.matches("^\\d{4}$")) throw new NumberFormatException("Illegal attempt string");
            final Game game = gamesRepository.findById(gameId)
                    .orElseThrow(() -> new IllegalArgumentException("Illegal game's id"));
            //Check player's nickname
            playersRepository.getByLogin(login)
                    .filter(game.getPlayer()::equals)
                    .orElseThrow(() -> new IllegalArgumentException("Illegal player's nickname"));
            //Check if game finished
            if (game.getAttempts()
                    .parallelStream()
                    .max(Comparator.comparingInt(Attempt::getId))
                    .map(attempt -> game.getGuessedNumber().equals(attempt.getNumber()))
                    .orElse(false)) throw new IllegalArgumentException("This game is already finished");
            Attempt attempt = new Attempt();
            attempt.setGame(game);
            attempt.setNumber(attemptNumber);
            attemptsRepository.saveAndFlush(attempt);
            if (attemptNumber.equals(game.getGuessedNumber()))
                return ResponseEntity.ok().headers(new HttpHeaders() {{
                    add("Content-Type", "text/plain; charset=utf-8");
                }}).body("Victory!");
            else {
                Integer bulls = 0;
                Integer cows = 0;
                final Iterator<String>
                        guessedNumberIterator =
                        Arrays.stream(game.getGuessedNumber().split("")).iterator();
                for (String symbol : attemptNumber.split("")) {
                    if (symbol.equals(guessedNumberIterator.next())) bulls++;
                    else if (game.getGuessedNumber().contains(symbol)) cows++;
                }
                return ResponseEntity.ok().headers(new HttpHeaders() {{
                    add("Content-Type", "text/plain; charset=utf-8");
                }}).body(String.format("%dB%dK", bulls, cows));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/rating")
    String rating(Model model) {
        model.addAttribute("rating", service.rating());
        return "rating";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    String playerNotFound(IllegalArgumentException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }
}