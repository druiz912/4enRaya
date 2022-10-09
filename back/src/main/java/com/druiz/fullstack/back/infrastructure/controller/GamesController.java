package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.GameService;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
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
    private Flux<BoardOutputDto> getAllGames(){
        log.info("****CARGANDO****");
        return gameService.findAllGames();
    }

    @PostMapping("/createGame")
    public Mono<BoardOutputDto> createGame(@RequestBody PlayerInputDto player) {
        log.info("******CARGANDO DATOS DE JUGADOR 1******");
        log.info("******DATOS DE JUGADOR HOST: " + player + "***********");
        return gameService.createGame(player);
    }

    @PostMapping("/connect/{idBoard}")
    public Mono<BoardOutputDto> connectToGame(@RequestBody PlayerInputDto player2,@PathVariable int idBoard) {
        log.info("******CARGANDO DATOS DE JUGADOR 2******");
        log.info("******DATOS DE JUGADOR 2: " + player2 + " conectado al tablero con id: " + idBoard + "******");
        return gameService.connectToGame(player2, idBoard);
    }

    @PostMapping("move")
    public Mono<BoardOutputDto> movimiento(@RequestBody MovimientoDto movimiento) {
        log.info("******CARGANDO DATOS******");
        log.info("******DATOS RECIBIDOS: " + movimiento + "******");
        gameService.colocarFicha(movimiento);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + movimiento.getIdBoard(),gameService.colocarFicha(movimiento));
        return gameService.getBoardById(movimiento.getIdBoard());
    }


    //

    @GetMapping("partidas")
    public Flux<BoardOutputDto> partidas() {
        log.info("*****Buscando todas las partidas con un solo jugador*****");
        return gameService.findBoardsWithOnePlayer();
    }




}