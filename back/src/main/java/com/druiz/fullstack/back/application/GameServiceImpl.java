package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.application.port.GameService;
import com.druiz.fullstack.back.config.exceptions.NotFoundException;
import com.druiz.fullstack.back.config.exceptions.UnprocessableException;
import com.druiz.fullstack.back.domain.GameStatus;
import com.druiz.fullstack.back.domain.Jugador;
import com.druiz.fullstack.back.domain.Tablero;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.JugadorInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.TableroOutputDto;
import com.druiz.fullstack.back.infrastructure.repo.PlayerRepo;
import com.druiz.fullstack.back.infrastructure.repo.TableroRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    private TableroRepo tableroRepo;

    @Autowired
    private PlayerRepo playerRepo;


    /***** Crear una partida --> Board & Player *****/

    /**
     * Método que crea un juego nuevo.
     *
     * @param playerInputDto Contiene la información del jugador
     * @return TableroOutputDto Contiene la información del tablero y del jugador
     */
    @Override
    public Mono<TableroOutputDto> createGame(JugadorInputDto playerInputDto) {
        // 1. Verificamos si el jugador ya existe en la base de datos
        Mono<Jugador> jugadorGuardado = findJugadorAndSaveOrCreate(playerInputDto);

        // 2. Creamos el tablero
        Tablero tablero = new Tablero();
        // 2.1 Añadimos el número de columnas
        tablero.setColumnas(7);
        // 2.2 Añadimos el número de filas
        tablero.setFilas(6);
        // 2.3 Asignamos matriz
        tablero.setFichas();
        // 2.4 Si la lista está vacía (estamos creando una partida)
        if (tablero.getJugadores().isEmpty()) {
            // Añadimos al jugador
            tablero.addJugador(Objects.requireNonNull( // mapeamos a Entidad
                    jugadorGuardado.map(player -> new Jugador(player.getId(), player.getUserPlayer()))
                            // .block() Se utiliza para bloquear el hilo de ejecución.
                            // Hasta que el Mono se complete y regrese un resultado no se ejecutará
                            .block()));
        } else { // Si la lista de Jugadores no está vacía, no se puede crear la partida
            throw new UnprocessableException("La partida ya tiene un jugador, debes conectarte con el id del tablero");
        }
        // 3. Guardamos el tablero en la base de datos
        Mono<Tablero> newTableroDB = tableroRepo.save(tablero)
                .onErrorResume(e -> Mono.error(new UnprocessableException("Failed to save board")));

        // 5. Devolvemos la información del tablero y del jugador en un DTO
        return newTableroDB
                .zipWith(jugadorGuardado, (board, player) -> {
                    // 5.1 Utilizamos el método zipWith de Mono para combinar la información de los dos objetos
                    // 5.2 El método recibe como primer parámetro el Mono que deseamos combinar con otro Mono
                    // 5.3 El segundo parámetro es una función BiFunction que se encarga de combinar los dos objetos
                    // 5.4 La función recibe como primer parámetro el objeto del primer Mono (board) y el segundo objeto del segundo Mono (player)
                    // 5.5 La función regresa un objeto TableroOutputDto con la información combinada
                    return new TableroOutputDto(board, player);
                    /* Se puede sustituir por lambda:
                     *  return newTableroDB.zipWith(newPlayerDB, TableroOutputDto::new);
                     *  */
                });
    }


    /**
     * Conecta a un segundo jugador a una partida existente con un tablero ya creado.
     *
     * @param player2 DTO que representa al segundo jugador que se quiere conectar a la partida.
     * @param idBoard ID del tablero al que se quiere conectar el segundo jugador.
     * @return Mono que representa el tablero con los datos actualizados después de conectar el segundo jugador.
     */
    @Override
    public Mono<TableroOutputDto> connectToGame(JugadorInputDto player2Input, int idBoard) {
        // 1. Verificamos si el jugador ya existe en la base de datos
        var newPlayer2InDB = findJugadorAndSaveOrCreate(player2Input);



        // 2. Una vez guardado el jugador2 (newPlayer2InDB), buscamos el tablero con el ID especificado
        return newPlayer2InDB.flatMap(jugador2 -> tableroRepo.findById(idBoard)
                        // 6. En caso de no encontrar el tablero, lanzamos una excepción
                        .switchIfEmpty(Mono.error(new NotFoundException("Not found board with id " + idBoard)))
                        .flatMap(tablero -> {
                            // Nos aseguramos que haya otro jugador ya añadido
                            if (tablero.getJugadores().size() != 0){
                                // 7. Asignamos el ID del jugador 2 al tablero
                                tablero.addJugador(newPlayer2InDB.map(
                                                player -> new Jugador(player.getId(), player.getUserPlayer()))
                                        .onErrorResume(e -> Mono.error(new UnprocessableException("Player is null")))
                                        // .block() Se utiliza para bloquear el hilo de ejecución.
                                        // Hasta que el Mono se complete y regrese un resultado no se ejecutará
                                        .block());
                            }
                            // 9. Guardamos los cambios en la base de datos
                            return tableroRepo.save(tablero)
                                    // 10. Convertimos la instancia de Tablero a TableroOutputDto
                                    .map(TableroOutputDto::new)
                                    // 11. En caso de error al guardar los cambios, lanzamos una excepción
                                    .switchIfEmpty(Mono.error(new UnprocessableException("Failed to save board")));
                        }))
                // 12. En caso de error en cualquier paso, lanzamos una excepción
                .onErrorResume(e -> Mono.error(new UnprocessableException("Could not connect to game")));
    }



    /***** LÓGICA DE LA PARTIDA *****/
/*
    @Override
    public Mono<Void> colocarFicha(MovimientoDto movimiento) {
        Mono<Tablero> boardMono = tableroRepo.findById(movimiento.getIdBoard())
                .switchIfEmpty(Mono.error(new NotFoundException("Could not find board with id " + movimiento.getIdBoard())));
        return Mono.just(boardMono
                        .subscribe(tableroMono1 -> realizarMovimiento(movimiento, tableroMono1)))
                .then();
    }
*/
/*
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

        tableroRepo.save(tablero).subscribe();
    }


    /***** FIND *****/
    @Override
    public Flux<TableroOutputDto> findAllGames() {
        return tableroRepo.findAll()
                .map(TableroOutputDto::new)
                .doOnNext(b -> log.info("procesando: " + b.toString()))
                .switchIfEmpty(Flux.error(new NotFoundException("No games found")));
    }

    @Override
    public Mono<TableroOutputDto> findBoardById(int idBoard) {
        return tableroRepo.findById(idBoard)
                .map(TableroOutputDto::new)
                .switchIfEmpty(Mono.error(new NotFoundException("No")));
    }

    @Override
    public Flux<TableroOutputDto> findBoardsWithOnePlayer() {
        return tableroRepo.findAllOnlyOnePlayer()
                .map(TableroOutputDto::new)
                .doOnNext(b -> log.info("procesando: " + b.toString()))
                .switchIfEmpty(Flux.error(new NotFoundException("Not found board with one player")));
    }


    //
    @Override
    public Mono<Void> deleteBoardById(int boardId) {
        return tableroRepo.deleteById(boardId);
    }


    /* FUNCIONES AUXILIARES */

    /**
     * Esta función se encarga de verificar si existe un jugador en la base de datos con el
     * nombre de usuario especificado en JugadorInputDto. Si no existe, se guarda el jugador en la BB DD.
     * @param newJugadorInput Objeto con los datos del jugador a comprobar y crear
     * @return Mono con el Jugador encontrado o creado
     */
    private Mono<Jugador> findJugadorAndSaveOrCreate(JugadorInputDto newJugadorInput) {
        // 1. Verificamos si existe en la BB DD
        return playerRepo.findByUserPlayer(newJugadorInput.getUserPlayer())
                // 2. Añadimos el jugador si lo que nos ha devuelto el objeto exists está vacío
                .switchIfEmpty(Mono.just(new Jugador(newJugadorInput)))
                // 3. Añadimos mapeo de errores
                .flatMap(playerRepo::save).onErrorResume(e -> Mono.error(new UnprocessableException("Failed to save player")));
    }

}