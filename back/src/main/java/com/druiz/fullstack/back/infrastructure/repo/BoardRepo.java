package com.druiz.fullstack.back.infrastructure.repo;

import com.druiz.fullstack.back.domain.Board;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BoardRepo extends ReactiveCrudRepository<Board, Integer> {

    
}
