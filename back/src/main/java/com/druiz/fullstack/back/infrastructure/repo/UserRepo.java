package com.druiz.fullstack.back.infrastructure.repo;

import com.druiz.fullstack.back.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepo extends ReactiveCrudRepository<User, Integer> {
    User findByUsername(String username);
}
