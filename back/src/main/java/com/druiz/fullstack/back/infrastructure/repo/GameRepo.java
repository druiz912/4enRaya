package com.druiz.fullstack.back.infrastructure.repo;

import com.druiz.fullstack.back.domain.Game;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GameRepo extends ReactiveCrudRepository<Game, Integer> {

    Mono<Game> findByPlayer1(String player1);
}
