package com.druiz.fullstack.back.application.port;

import com.druiz.fullstack.back.infrastructure.controller.dto.output.JugadorOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {

    /* Obtener lista de jugadores */
    Flux<JugadorOutputDto> findAllPlayers();

    /* Obtener jugador por ID */
    Mono<JugadorOutputDto> findPlayerById(int idPlayer);

    /* Obtener jugador por user */
    Mono<JugadorOutputDto> findPlayerByUserPlayer(String userPlayer);

    /* Borrar a un jugador por ID */
    Mono<Void> deletePlayerById(int boardId);
}
