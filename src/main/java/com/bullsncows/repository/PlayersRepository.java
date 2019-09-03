package com.bullsncows.repository;

import com.bullsncows.entity.Game;
import com.bullsncows.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayersRepository extends JpaRepository<Player, Integer> {
    @Query("select pe from Player pe where pe.login=:login")
    Optional<Player> getByLogin(
            @Param("login")
                    String login);

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query("select pe.games from Player pe where pe.login=:login")
    List<Game> getGamesByLogin(
            @Param("login")
                    String login);
}
