package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.application.port.GameService;
import com.druiz.fullstack.back.config.exceptions.NotFoundException;
import com.druiz.fullstack.back.config.exceptions.UnprocessableException;
import com.druiz.fullstack.back.domain.Jugador;
import com.druiz.fullstack.back.domain.Tablero;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.JugadorInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.FichaInputDto;
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
            Jugador jugadorHost = mapMonoToJugador(Objects.requireNonNull(jugadorGuardado));
            tablero.addJugador(jugadorHost);
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
     * @param player2Input DTO que representa al segundo jugador que se quiere conectar a la partida.
     * @param idBoard ID del tablero al que se quiere conectar el segundo jugador.
     * @return Mono que representa el tablero con los datos actualizados después de conectar el segundo jugador.
     */
    @Override
    public Mono<TableroOutputDto> connectToGame(JugadorInputDto player2Input, int idBoard) {
        // 1. Verificamos si el jugador ya existe en la base de datos
        Mono<Jugador> jugador2 = findJugadorAndSaveOrCreate(player2Input);

        // 2. Verificamos si existe de verdad el tablero
        Mono<Tablero> tablero = tableroRepo.findById(idBoard)
                // 2.1. Si el tablero no existe, lanzamos una excepción
                .switchIfEmpty(Mono.error(new NotFoundException("Not found board with id " + idBoard)));

        // 3. Continuamos con la ejecución si el tablero existe
        tablero.flatMap(board -> {
            // 3.1. Nos aseguramos de que haya otro jugador ya añadido
            if (board.getJugadores().size() != 0) {
                // 3.2. Asignamos el ID del jugador 2 al tablero
                Jugador jugadorMap = mapMonoToJugador(jugador2);
                board.addJugador(jugadorMap);
            }

            // 4. Guardamos los cambios realizados del tablero
            Mono<TableroOutputDto> tablero2 = tableroRepo.save(board)
                    // 4.1. Convertimos la instancia de Tablero a TableroOutputDto
                    .map(TableroOutputDto::new)
                    // 4.2. En caso de error al guardar los cambios, lanzamos una excepción
                    .switchIfEmpty(Mono.error(new UnprocessableException("Failed to save board")));

            // 5. En caso de error al conectar al juego, lanzamos una excepción
            return tablero2.onErrorResume(e -> Mono.error(new UnprocessableException("Could not connect to game")));
        });

        // 6. El método debe retornar algo
        return null;
    }

    @Override
    public Mono<Void> colocarFicha(FichaInputDto movimiento) {
        return Mono.just(movimiento)
                .flatMap(this::realizarMovimiento)
                .then();
    }

    /**
     * Realiza un movimiento en el juego 4 en raya.
     *
     * @param movimiento DTO que contiene la información sobre el movimiento, incluyendo el ID del tablero, el ID del jugador y la posición en la que se desea colocar la ficha.
     * @return Mono<Void> Un Mono vacío que se resuelve cuando se realiza con éxito el movimiento.
     */
    private Mono<Void> realizarMovimiento(FichaInputDto movimiento) {

        // Primero, se busca el tablero en el repositorio con el ID proporcionado en el DTO de movimiento.
        tableroRepo.findById(movimiento.getIdTablero())
                // Si no se encuentra el tablero, se genera un error NotFoundException.
                .switchIfEmpty(Mono.error(new NotFoundException(
                        "No se ha encontrado el tablero con el id: " + movimiento.getIdTablero())));

        // Después, se busca el jugador en el repositorio con el ID proporcionado en el DTO de movimiento.
        Mono<Jugador> jugadorMono = playerRepo.findById(movimiento.getIdJugador())
                // Si no se encuentra el jugador, se genera un error UnprocessableException.
                .onErrorResume(e -> Mono.error(new UnprocessableException(
                        "No se pudo encontrar al jugador con id: " + movimiento.getIdJugador())));
        // Mapeamos el jugador a entidad para uso próximo
        Jugador jugador = mapMonoToJugador(jugadorMono);

        // A continuación, se realiza el movimiento en el tablero.
        return playerRepo.findById(movimiento.getIdJugador())
                // Se busca el tablero en el repositorio a partir del ID proporcionado en el DTO de movimiento.
                .flatMap(player -> tableroRepo.findById(movimiento.getIdTablero()))
                // Una vez que tenemos el tablero...
                .flatMap(board -> {
                    // Se llama al método que realiza el movimiento en el tablero.
                    board.realizarMovimiento(
                            movimiento.getFila(), movimiento.getColumna(), jugador);
                    // Finalmente, se guarda el tablero actualizado en el repositorio.
                    return tableroRepo.save(board);
                })
                // then() se usa para indicar que la operación de guardar el Tablero se ha completado con éxito.
                // Esto permite que se siga con el código siguiente una vez que se haya actualizado el tablero.
                .then();
    }

    /* ***-***-***-***-***-*** FUNCIONES DE BÚSQUEDA EN LA BB DD ***-***-***-***-***-*** */

    /**
     * Este método se encarga de encontrar todos los juegos (tableros) disponibles.
     *
     * @return Un flujo de {@link TableroOutputDto} que representa todos los juegos encontrados.
     * @throws NotFoundException si no se encuentra ningún juego.
     */
    @Override
    public Flux<TableroOutputDto> findAllGames() {
        return tableroRepo.findAll()
                .map(TableroOutputDto::new)
                .switchIfEmpty(Flux.error(new NotFoundException("No hay tableros")));
    }

    /**
     * Este método se encarga de encontrar un juego (tablero) específico por su identificador.
     *
     * @param idBoard El identificador del juego a encontrar.
     * @return Un {@link Mono} de {@link TableroOutputDto} que representa el juego encontrado.
     * @throws NotFoundException si no se encuentra el juego con el identificador especificado.
     */
    @Override
    public Mono<TableroOutputDto> findBoardById(int idBoard) {
        if (idBoard <= 0) {
            return Mono.error(new IllegalArgumentException("Board ID must be greater than 0"));
        }
        return tableroRepo.findById(idBoard)
                .map(TableroOutputDto::new)
                .switchIfEmpty(Mono.error(new NotFoundException("No")));
    }

    /**
     * Este método se encarga de encontrar todos los juegos (tableros) que solo tienen un jugador conectado.
     *
     * @return Un flujo de {@link TableroOutputDto} que representa todos los juegos encontrados con un solo jugador.
     * @throws NotFoundException si no se encuentra ningún juego con un solo jugador.
     */
    @Override
    public Flux<TableroOutputDto> findBoardsWithOnePlayer() {
        return tableroRepo.findAllOnlyOnePlayer()
                .map(TableroOutputDto::new)
                .doOnNext(b -> log.info("procesando: " + b.toString()))
                .switchIfEmpty(Flux.error(new NotFoundException("Not found board with one player")));
    }

    /**
     * Este método se encarga de eliminar un juego (tablero) específico por su identificador.
     *
     * @param idBoard El identificador del juego a eliminar.
     * @return Un {@link Mono} vacío que indica el resultado de la operación.
     */
    @Override
    public Mono<Void> deleteBoardById(int idBoard) {
        if (idBoard <= 0) {
            return Mono.error(new IllegalArgumentException("Board ID must be greater than 0"));
        }
        return tableroRepo.deleteById(idBoard);
    }


    /* ***-***-***-***-***-*** FUNCIONES AUXILIARES ***-***-***-***-***-*** */

    /**
     * Esta función se encarga de verificar si existe un jugador en la base de datos con el
     * nombre de usuario especificado en JugadorInputDto. Si no existe, se guarda el jugador en la BB DD.
     *
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

    /**
     * Mapea un mono de JugadorGuardado a un objeto Jugador
     *
     * @param jugadorGuardado Mono<Jugador> a ser mapeado
     * @return Un objeto Jugador
     */
    private Jugador mapMonoToJugador(Mono<Jugador> jugadorGuardado) {
        return jugadorGuardado
                .map(player -> new Jugador(player.getId(), player.getUserPlayer()))
                .onErrorResume(e -> Mono.error(new UnprocessableException("Failed to map player")))
                .block();
    }

}
