package com.druiz.fullstack.back.infrastructure.repo;


import com.druiz.fullstack.back.domain.Jugador;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayerRepo extends ReactiveCrudRepository<Jugador,Integer> {

    Mono<Jugador> findByUserPlayer(String userPlayer);
}