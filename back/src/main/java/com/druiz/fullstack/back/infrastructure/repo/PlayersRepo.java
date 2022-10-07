package com.druiz.fullstack.back.infrastructure.repo;


import com.druiz.fullstack.back.domain.Player;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PlayersRepo extends ReactiveCrudRepository<Player,Integer> {


}