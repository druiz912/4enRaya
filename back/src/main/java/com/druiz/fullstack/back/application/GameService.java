package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.domain.Game;
import com.druiz.fullstack.back.domain.GamePlay;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.GameOutputDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    Mono<GameOutputDto> createGame(PlayerInputDto player);
    Flux<Game> getGames();
    Mono<Object> connectToGame(PlayerInputDto player2, int gameId);
    Mono<Game> gameplay(GamePlay gamePlay);

}