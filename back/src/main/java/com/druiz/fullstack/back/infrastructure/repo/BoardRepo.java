package com.druiz.fullstack.back.infrastructure.repo;

import com.druiz.fullstack.back.domain.Board;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BoardRepo extends ReactiveCrudRepository<Board, Integer> {

    @Query("SELECT b.*" +
            " FROM board b" +
            " WHERE b.player2 IS NULL")
    Flux<Board> findAllOnlyOnePlayer();

    @Query("SELECT b.*" +
            " FROM board b" +
            " WHERE b.player2 IS NOT NULL")
    Flux<Board> findAllTwoPlayers();

    
}
