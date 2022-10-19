package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.port.PlayerService;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.PlayerOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("players")
@CrossOrigin
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @GetMapping("id/{playerId}")
    public Mono<PlayerOutputDto> getPlayerById(@PathVariable("idPlayer") int playerId) {
        log.info("***** CARGANDO DATOS  -->" + playerId + "*****");
        return playerService.findPlayerById(playerId);
    }

    @GetMapping("user/{userPlayer}")
    public Mono<PlayerOutputDto> getPlayerByUserPlayer(@PathVariable String userPlayer) {
        log.info("***** CARGANDO DATOS  -->" + userPlayer + "*****");
        return playerService.findPlayerByUserPlayer(userPlayer);
    }

    @GetMapping
    public Flux<PlayerOutputDto> getAllPlayers(){
        return playerService.findAllPlayers();
    }

    @DeleteMapping("/{playerId}")
    public Mono<ResponseEntity<Void>> deletePlayerById(@PathVariable int playerId){
        return playerService.findPlayerById(playerId)
                .flatMap(p -> playerService.deletePlayerById(p.getIdPlayer())
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
