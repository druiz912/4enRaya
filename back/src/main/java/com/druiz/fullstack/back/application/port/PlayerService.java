package com.druiz.fullstack.back.application.port;

import com.druiz.fullstack.back.infrastructure.controller.dto.output.PlayerOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerService {

    /* Obtener lista de jugadores */
    Flux<PlayerOutputDto> findAllPlayers();

    /* Obtener jugador por ID */
    Mono<PlayerOutputDto> findPlayerById(int idPlayer);

    /* Obtener jugador por user */
    Mono<PlayerOutputDto> findPlayerByUserPlayer(String userPlayer);

    /* Borrar a un jugador por ID */
    Mono<Void> deletePlayerById(int boardId);
}
