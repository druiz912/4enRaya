package com.druiz.fullstack.back.application.port;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.JugadorInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.TableroOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    /* FIND */

    /* Obtener todos los tableros */
    Flux<TableroOutputDto> findAllGames();
    /* Obtener un tablero según el ID */
    Mono<TableroOutputDto> findBoardById(int idBoard);
    /* Obtener tableros con un solo jugador */
    Flux<TableroOutputDto> findBoardsWithOnePlayer();
    //
    /* Borrar tablero según el ID */
    Mono<Void> deleteBoardById(int boardId);

    /* GAME */

    /* Crear una partida */

    Mono<TableroOutputDto> createGame(JugadorInputDto playerInputDto);

    /* Conectarse a una partida */
    Mono<TableroOutputDto> connectToGame(JugadorInputDto player2, int idBoard);

    Mono<Void> colocarFicha(MovimientoDto movimiento);





}