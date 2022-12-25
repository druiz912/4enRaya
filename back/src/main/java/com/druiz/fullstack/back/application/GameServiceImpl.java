package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.application.port.GameService;
import com.druiz.fullstack.back.config.exceptions.NotFoundException;
import com.druiz.fullstack.back.config.exceptions.UnprocessableException;
import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.GameStatus;
import com.druiz.fullstack.back.domain.Player;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.MovimientoDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import com.druiz.fullstack.back.infrastructure.repo.BoardRepo;
import com.druiz.fullstack.back.infrastructure.repo.PlayerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private PlayerRepo playerRepo;



    /***** Crear una partida --> Board & Player *****/

    @Override
    public Mono<BoardOutputDto> createGame(PlayerInputDto playerInputDto) {
        // 1. Pasamos Input a Entity con el constructor creado en la entidad
        Player newPlayerInput = new Player(playerInputDto);
        // 2. Guardamos la entidad mapeada
        var newPlayerDB = playerRepo.save(newPlayerInput)
                .onErrorResume(e -> Mono.error(new UnprocessableException("Failed to save player")));

        // 3. Creamos el tablero
        Board tablero = new Board();
        // 3.1 Añadimos el número de columnas
        tablero.setNumColumns(7);
        // 3.2 Añadimos el número de filas
        tablero.setNumRows(6);
        // 3.3 Asignamos matriz
        tablero.setMatriz(new int[tablero.getNumRows()][tablero.getNumColumns()]);
        // 3.4 Asignamos status
        tablero.setStatus(GameStatus.NEW);


        // Devolvemos un Mono<Player> por ende usamos flatMap para poder acceder al ID del jugador 1 previamente guardado.
        return newPlayerDB.flatMap(player1 -> {
            tablero.setIdHostPlayer(player1.getId());
            tablero.setIdGuestPlayer(0);
            return boardRepo.save(tablero)
                    .map(BoardOutputDto::new)
                    .switchIfEmpty(Mono.error(new UnprocessableException("Failed to save board")));
        })
                .onErrorResume(e -> Mono.error(new UnprocessableException("Failed to generate game")));
    }
    /***** Conectarse a una partida ≥ Player & IdBoard *****/

    @Override
    public Mono<BoardOutputDto> connectToGame(PlayerInputDto player2, int idBoard) {
        Player newPlayer2 = new Player(player2);
        var newPlayer2InDB = playerRepo.findByUserPlayer(player2.getUserPlayer())
                .switchIfEmpty(playerRepo.save(newPlayer2))
                .onErrorResume(e -> Mono.error(new UnprocessableException("Failed to save player")));

        // Una vez guardado el jugador2 (newPlayer2InDB) le damos el id del tablero
        return newPlayer2InDB.flatMap(jugador2 -> boardRepo.findById(idBoard)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found board with id " + idBoard)))
                //
                .flatMap(board1 -> {
                    // Le damos los valores del jugador 2
                    board1.setIdGuestPlayer(jugador2.getId());
                    board1.setStatus(GameStatus.IN_PROGRESS);
                    // Con los nuevos valores la volvemos a guardar
                    return boardRepo.save(board1).map(BoardOutputDto::new)
                            .switchIfEmpty(Mono.error(new UnprocessableException("Failed to save board")));

                })).onErrorResume(e -> Mono.error(new UnprocessableException("Could not connect to game")));
    }


    /***** LÓGICA DE LA PARTIDA *****/

    @Override
    public Mono<Void> colocarFicha(MovimientoDto movimiento) {
        Mono<Board> boardMono = boardRepo.findById(movimiento.getIdBoard())
                .switchIfEmpty(Mono.error(new NotFoundException("Could not find board with id " + movimiento.getIdBoard())));
        return Mono.just(boardMono
                        .subscribe(tableroMono1 -> realizarMovimiento(movimiento, tableroMono1)))
                .then();
    }


    private void realizarMovimiento(MovimientoDto movimiento, Board tablero) {
        // Comprueba posibilidad de movimiento
        if (tablero.getMatriz()[0][movimiento.getColumn()] != 0) {
            throw new UnprocessableException("Columna llena");
        }
        // Guardamos la matriz del tablero
        int[][] boardGame = tablero.getMatriz();
        int counterIndex = 0;

        for(int columns = 0; columns < 7; columns++){
            for(int rows = 0; rows < 6; rows++){
                if(boardGame[movimiento.getColumn()][rows] == 0) {
                    counterIndex = rows;
                }
            }
        }
        boardGame[movimiento.getColumn()][counterIndex] = movimiento.getValue();

        boardRepo.save(tablero).subscribe();
    }


    /***** FIND *****/
    @Override
    public Flux<BoardOutputDto> findAllGames() {
        return boardRepo.findAll()
                .map(BoardOutputDto::new)
                .doOnNext(b -> log.info("procesando: " + b.toString()))
                .switchIfEmpty(Flux.error(new NotFoundException("No games found")));
    }

    @Override
    public Mono<BoardOutputDto> findBoardById(int idBoard) {
        return boardRepo.findById(idBoard)
                .map(BoardOutputDto::new)
                .switchIfEmpty(Mono.error(new NotFoundException("No")));
    }

    @Override
    public Flux<BoardOutputDto> findBoardsWithOnePlayer() {
        return boardRepo.findAllOnlyOnePlayer()
                .map(BoardOutputDto::new)
                .doOnNext(b -> log.info("procesando: " + b.toString()))
                .switchIfEmpty(Flux.error(new NotFoundException("Not found board with one player")));
    }


    //
    @Override
    public Mono<Void> deleteBoardById(int boardId) {
        return boardRepo.deleteById(boardId);
    }



}