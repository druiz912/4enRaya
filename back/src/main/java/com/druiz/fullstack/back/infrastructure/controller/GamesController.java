package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.port.GameService;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/game")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GamesController {

    @Autowired
    private GameService gameService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("partidas")
    public Flux<BoardOutputDto> partidas() {
        log.info("***** Buscando todas las partidas con un solo jugador *****");
        return gameService.findBoardsWithOnePlayer();
    }

    @GetMapping("id/{boardId}")
    public Mono<BoardOutputDto> getBoardById(@PathVariable int boardId) {
        log.info("***** CARGANDO DATOS  -->" + boardId + "*****");
        return gameService.findBoardById(boardId);
    }


    @GetMapping
    private Flux<BoardOutputDto> getAllGames(){
        log.info("****CARGANDO****");
        return gameService.findAllGames();
    }

    @DeleteMapping("/{boardId}")
    public Mono<ResponseEntity<Void>> deleteBoardById(@PathVariable int boardId){
        return gameService.findBoardById(boardId)
                .flatMap(b -> gameService.deleteBoardById(b.getIdBoard())
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/createGame")
    public Mono<BoardOutputDto> createGame(@RequestBody PlayerInputDto player) {
        log.info("****** CARGANDO DATOS DE JUGADOR 1 ******");
        log.info("****** DATOS DE JUGADOR HOST: " + player.toString() + " ***********");
        return gameService.createGame(player);
    }

    @PostMapping("/connect/{boardId}")
    public Mono<BoardOutputDto> connectToGame(@RequestBody PlayerInputDto player2, @PathVariable int boardId) {
        log.info("****** CARGANDO DATOS DE JUGADOR 2 ******");
        log.info("****** DATOS DE JUGADOR 2: " + player2.toString() + " conectado al tablero con id: " + boardId + " ******");
        return gameService.connectToGame(player2, boardId);
    }

    @PostMapping("move")
    public Mono<BoardOutputDto> movimiento(@RequestBody MovimientoDto movimiento) {
        log.info("******CARGANDO DATOS******");
        log.info("******DATOS RECIBIDOS: " + movimiento + "******");
        gameService.colocarFicha(movimiento);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + movimiento.getIdBoard(),gameService.colocarFicha(movimiento));
        return gameService.findBoardById(movimiento.getIdBoard());
    }






}