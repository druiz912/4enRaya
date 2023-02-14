package com.druiz.fullstack.back.infrastructure.repo;

import com.druiz.fullstack.back.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepo extends ReactiveCrudRepository<User, Integer> {
    Mono<User> findByUsername(String username);
}
