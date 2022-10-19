package com.druiz.fullstack.front.application;

import com.druiz.fullstack.front.domain.Board;
import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(url = "http://localhost:8081/game", name = "board-back")
public interface BoardFeign {
    @GetMapping
    Flux<Board> getBoards();

}
