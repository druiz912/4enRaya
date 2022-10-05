package com.druiz.fullstack.back.infrastructure.repo;


import com.druiz.fullstack.back.domain.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PlayersRepo extends ReactiveCrudRepository<Player,Integer> {

    Mono<Player> findByName(String name);

}