package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.GameService;
import com.druiz.fullstack.back.domain.Game;
import com.druiz.fullstack.back.domain.GamePlay;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.ConnectInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.GameOutputDto;
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
    private Flux<Game> getGames(){
        return gameService.getGames();
    }

    @PostMapping("/start")
    public Mono<GameOutputDto> start(@RequestBody PlayerInputDto player){
        log.info("start new game request {}",player);
        return gameService.createGame(player);
    }

    @PostMapping("/connect")
    public Mono<Object> connect(@RequestBody ConnectInputDto request){
        log.info("connect request: {}",request);
        return gameService.connectToGame(request.getPlayer2(),request.getGameId());
    }

    @PostMapping("/gameplay")
    public Mono<Game> gameplay(@RequestBody GamePlay request){
        log.info("gameplay: {}",request);

        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(),gameService.gameplay(request));
        return gameService.gameplay(request);
    }

}