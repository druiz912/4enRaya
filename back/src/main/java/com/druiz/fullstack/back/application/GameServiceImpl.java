package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.config.exceptions.UnprocessableException;
import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.Player;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
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
    public Mono<BoardOutputDto> createGame(PlayerInputDto playerInputDto) {
        Player newPlayerInput = new Player(playerInputDto);
        var newPlayerDB = playersRepo.save(newPlayerInput);
        // 3. Creamos el tablero
        Board tablero = new Board();
        tablero.setMatriz(new int[6][7]);
        tablero.setNumColumns(7);
        tablero.setNumRows(6);
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
    public Mono<BoardOutputDto> connectToGame(PlayerInputDto player2, int idBoard) {
        Player newPlayer2 = new Player(player2);
        var newPlayer2InDB = playersRepo.save(newPlayer2);

        return newPlayer2InDB.flatMap(jugador2 -> boardRepo.findById(idBoard)
                .flatMap(board1 -> {
                    board1.setIdGuestPlayer(jugador2.getId());
                    return boardRepo.save(board1).map(BoardOutputDto::new)
                            .switchIfEmpty(Mono.error(new UnprocessableException("No se pudo unir a la partida")));
                }));
    }


    @Override
    public Flux<BoardOutputDto> findAllGames() {
        return boardRepo.findAll().map(BoardOutputDto::new);
    }

    @Override
    public Mono<BoardOutputDto> getBoardById (int idBoard) {
        return boardRepo.findById(idBoard).map(BoardOutputDto::new);
    }

    @Override
    public Flux<BoardOutputDto> findBoardsWithOnePlayer() {
        return boardRepo.findAllOnlyOnePlayer().map(BoardOutputDto::new);
    }


    @Override
    public Mono<Void> colocarFicha(MovimientoDto movimiento) {
        Mono<Board> boardMono = boardRepo.findById(movimiento.getIdBoard());
        return Mono.just(boardMono
                        .subscribe(tableroMono1 -> realizarMovimiento(movimiento, tableroMono1)))
                .then();
    }


    private void realizarMovimiento(MovimientoDto movimiento, Board tablero) {
    // Comprueba posibilidad de movimiento
    if (tablero.getMatriz()[0][movimiento.getColumn()] != 0) {
        throw new UnprocessableException("Columna llena");
    }
    int fila = -1;
    // Reflejamos en la matriz
    for (int i = 5; i >= 0; i--) {
        if (tablero.getMatriz()[i][movimiento.getColumn()] == 0) {
            tablero.getMatriz()[i][movimiento.getColumn()] = movimiento.getIdPlayer();
            fila = i;
            break;
        }
    }
    boardRepo.save(tablero).subscribe();
}

}