package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {



    // Crear una partida, por ende se conecta jugador 1
    Mono<BoardOutputDto> createGame(PlayerInputDto playerInputDto);
    // Conectarse a una partida, por ende se conecta jugador 2
    Mono<BoardOutputDto> connectToGame(PlayerInputDto player2, int idBoard);


    // Obtener todos los tableros
    Flux<BoardOutputDto> findAllGames();
    // Obtener un tablero según el id
    Mono<BoardOutputDto> getBoardById(int idBoard);
    // Obtener tableros con un solo jugador
    Flux<BoardOutputDto> findBoardsWithOnePlayer();


    // EN PARTIDA
    Mono<Void> colocarFicha(MovimientoDto movimiento);
}