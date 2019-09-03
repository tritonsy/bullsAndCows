package com.bullsncows.repository;

import com.bullsncows.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface GamesRepository extends JpaRepository<Game, Integer> {
    /**
     * Lists all unfinished games of specified player. Finished game – if it's last attempt is equal guessed number of
     * this game or if game has no attempts.
     *
     * @param login nickname of player
     * @return list of unfinished games
     */
    @Query(value = "select" +
            "  game.id," +
            "  game.date_of_game," +
            "  game.player," +
            "  game.guessed_number " +
            "from public.game as game" +
            "  join (select" +
            "          g.id      as game," +
            "          max(a.id) as at" +
            "        from public.game g left join public.attempt a on g.id = a.game" +
            "        group by g.id) mid on game.id = mid.game " +
            "where ( mid.at isnull or" +
            "      (game.guessed_number != (select number" +
            "                               from public.attempt" +
            "                               where id = mid.at)))" +
            "      and player = (select id" +
            "                    from public.player" +
            "                    where login = :login) " +
            "order by game.date_of_game desc;", nativeQuery = true)
    List<Game> getNotEndedGamesByPlayerLogin(
            @Param("login")
                    String login);

    /**
     * Rating is counted just by finished games. Finished game – if it's last attempt is equal guessed number of
     * this game.
     *
     * @return sorted by average attempts for each player list with his id and login
     */
    @Query(value = "select" +
            "  p.login," +
            "  av.avg " +
            "from public.player p" +
            "  join" +
            "  (select" +
            "     player," +
            "     avg(c.count) as avg" +
            "   from public.game g" +
            "     join (select" +
            "             ended.id," +
            "             count(attempt.id) as count" +
            "           from (select id" +
            "                 from public.game" +
            "                   join (select" +
            "                           game," +
            "                           max(id) as at" +
            "                         from public.attempt" +
            "                         group by game) mid on game.id = mid.game" +
            "                 where game.guessed_number = (select number" +
            "                                              from public.attempt" +
            "                                              where id = mid.at)) ended" +
            "             join public.attempt on ended.id = attempt.game" +
            "           group by ended.id) c on g.id = c.id" +
            "   group by player) av on p.id = av.player " +
            "order by av.avg;", nativeQuery = true)
    Stream<Object[]> rating();
}
