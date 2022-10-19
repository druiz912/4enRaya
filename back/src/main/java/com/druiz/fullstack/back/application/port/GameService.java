package com.druiz.fullstack.back.application.port;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    /* FIND */

    /* Obtener todos los tableros */
    Flux<BoardOutputDto> findAllGames();
    /* Obtener un tablero según el ID */
    Mono<BoardOutputDto> findBoardById(int idBoard);
    /* Obtener tableros con un solo jugador */
    Flux<BoardOutputDto> findBoardsWithOnePlayer();
    //
    /* Borrar tablero según el ID */
    Mono<Void> deleteBoardById(int boardId);

    /* GAME */

    /* Crear una partida */
    Mono<BoardOutputDto> createGame(PlayerInputDto playerInputDto);
    /* Conectarse a una partida */
    Mono<BoardOutputDto> connectToGame(PlayerInputDto player2, int idBoard);

    Mono<Void> colocarFicha(MovimientoDto movimiento);





}