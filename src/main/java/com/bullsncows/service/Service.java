package com.bullsncows.service;

import com.bullsncows.controller.RatingItem;
import com.bullsncows.controller.RegistrationForm;
import com.bullsncows.entity.Game;
import com.bullsncows.entity.Player;
import com.bullsncows.repository.GamesRepository;
import com.bullsncows.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    PlayersRepository playersRepository;
    @Autowired
    GamesRepository gamesRepository;

    public Integer save(RegistrationForm form) {
        return playersRepository.saveAndFlush(new Player(form.getLogin(), form.getPassword())).getId();
    }

    /**
     * Find player from database by nickname or returns Optional.empty, if player not exists or @login is null
     *
     * @param login     nickname of player to find
     * @param loadGames do u want to load his games? it's lazy fetch strategy field
     * @return Optional of player, if he exists in database, Optional.empty otherwise or if login is null
     */
    public Optional<Player> loadPlayer(String login, Boolean loadGames) {
        if (login == null) return Optional.empty();
        return playersRepository.getByLogin(login).map(player -> {
            if (loadGames) player.setGames(playersRepository.getGamesByLogin(login));
            return player;
        });
    }


    /**
     * Generates new game, related with specified player
     *
     * @param player nickname of player
     * @return new game for player
     * @throws IllegalArgumentException if nickname doesn't exists in db
     */
    public Game newGame(String player) throws IllegalArgumentException {
        Game game = new Game();
        game.setPlayer(playersRepository.getByLogin(player)
                .orElseThrow(() -> new IllegalArgumentException("This player doesn't exists")));
        game.setGuessedNumber(new Random(System.currentTimeMillis()).ints(0, 9)
                .distinct()
                .limit(4)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining("")));
        return gamesRepository.saveAndFlush(game);
    }

    /**
     * Method generates rating of players. At first it gets players who finished at least one game and counts their
     * average attempts. Then it retains their nicknames from list of all players, give them 0 average number. Then it
     * concatenates players with at least one game with others and collects it to list
     *
     * @return list representatives players rating
     */
    @Transactional(readOnly = true)
    public List<RatingItem> rating() {
        final List<RatingItem>
                ratingWithCompletedGames =
                gamesRepository.rating().parallel().map(this::castRating).collect(Collectors.toList());
        final List<String>
                playersWithoutCompletedGames =
                playersRepository.findAll()
                        .parallelStream()
                        .map(Player::getLogin)
                        .collect(Collectors.collectingAndThen(Collectors.toList(), LinkedList::new));
        playersWithoutCompletedGames.removeAll(ratingWithCompletedGames.parallelStream()
                .map(RatingItem::getLogin)
                .collect(Collectors.toList()));
        return Stream.concat(ratingWithCompletedGames.stream(),
                playersWithoutCompletedGames.stream().map(this::loginToItem))
                .collect(Collectors.toList());
    }

    private RatingItem castRating(Object[] rating) {
        RatingItem ratingItem = new RatingItem();
        ratingItem.setLogin((String) rating[0]);
        ratingItem.setAverage(String.format("%.2f", ((BigDecimal) rating[1]).doubleValue()));
        return ratingItem;
    }

    private RatingItem loginToItem(String login) {
        RatingItem ratingItem = new RatingItem();
        ratingItem.setLogin(login);
        ratingItem.setAverage("0.0");
        return ratingItem;
    }
}
