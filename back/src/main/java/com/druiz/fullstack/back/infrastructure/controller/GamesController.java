package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.GameService;
import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.Player;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/game")
@CrossOrigin
public class GamesController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    private Flux<Board> getGames(){
        log.info("****CARGANDO****");
        return gameService.getGames();
    }

    @PostMapping("/createGame")
    public Mono<BoardOutputDto> createGame(@RequestBody PlayerInputDto player) {
        log.info("******DATOS DE JUGADOR HOST: " + player + "***********");
        return gameService.createGame(player);
    }

}