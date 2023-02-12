package com.druiz.fullstack.back.infrastructure.repo;

import com.druiz.fullstack.back.domain.Tablero;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TableroRepo extends ReactiveCrudRepository<Tablero, Integer> {

    @Query("SELECT b.*" +
            " FROM board b" +
            " WHERE b.player2 IS NULL")
    Flux<Tablero> findAllOnlyOnePlayer();

    @Query("SELECT b.*" +
            " FROM board b" +
            " WHERE b.player2 IS NOT NULL")
    Flux<Tablero> findAllTwoPlayers();
}
