package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.config.exceptions.UnprocessableException;
import com.druiz.fullstack.back.config.mappers.*;
import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.Player;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import com.druiz.fullstack.back.infrastructure.repo.BoardRepo;
import com.druiz.fullstack.back.infrastructure.repo.PlayersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService {

    // Lógica del juego --> Con la entidad Board & Player

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private PlayersRepo playersRepo;


    @Override
    public Mono<BoardOutputDto> connectToGame(PlayerInputDto player2, int gameId) {
        return null;
    }


    @Override
    public Mono<BoardOutputDto> createGame(PlayerInputDto playerInputDto) {
        Player newPlayerInput = new Player(playerInputDto);
        var newPlayerDB = playersRepo.save(newPlayerInput);
        // 3. Creamos el tablero
        Board tablero = new Board();
        tablero.setMatriz(new int[6][7]);
        tablero.setColumns(7);
        tablero.setRows(6);
        // Devolvemos un Mono<Player> por ende usamos flatMap para poder acceder al id del jugador 1 previamente guardado.
        return newPlayerDB.flatMap(player1 -> {
            tablero.setIdHostPlayer(player1.getId());
            tablero.setIdGuestPlayer(0);
            // El boardRepo.save(tablero) --> Nos devuelve un Mono<Board> hay que mapearlo a Mono<BoardOutputDto>
            return boardRepo.save(tablero).map(BoardOutputDto::new)
                    .switchIfEmpty(Mono.error(new UnprocessableException("No se pudo crear el tablero")));
        });
    }

    @Override
    public Flux<BoardOutputDto> findAllGames() {
        return boardRepo.findAll().map(BoardOutputDto::new);
    }


}