package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.Player;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    // Conectarse a una partida, por ende se conecta jugador 2
    Mono<BoardOutputDto> connectToGame(PlayerInputDto player2, int gameId);

    // Crear una partida, por ende se conecta jugador 1
    Mono<BoardOutputDto> createGame(PlayerInputDto playerInputDto);

    Flux<Board> getGames();


}