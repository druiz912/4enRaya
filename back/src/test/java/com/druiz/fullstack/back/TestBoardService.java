package com.druiz.fullstack.back;

import com.druiz.fullstack.back.application.port.GameService;
import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;

@WebFluxTest
class TestBoardService {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    GameService gameService;

    @Test
    void testGetAllBoards() {
        // 1. Creamos nuestra salida
        Board board = new Board();
        // 1.1 Seteamos campos
        board.setId(1); board.setIdHostPlayer(1); board.setIdGuestPlayer(2); board.setNumColumns(7); board.setNumRows(6);
        board.setMatriz(new int[6][7]);
        // 1.2 Creamos el Flux de output pasándole la entidad board
        Flux<BoardOutputDto> boardFlux = Flux.just(new BoardOutputDto(board));
        /* 3. Usamos el método when de Mockito para que cuando se use boardRepo.findAll()
                                               (Usado en el endpoint al que voy a llamar)
           3.1 thenReturn -> ¿Qué debería devolver cuando se llame al método anterior mencionado en Mockito.when()?
                            - Pues vas a devolver boardFlux
        */
        Mockito.when(gameService.findAllGames()).thenReturn(boardFlux);

        // 4. Usamos WebTestClient para probar el endpoint
        webTestClient
                // 4.1 Usamos el método GET
                .get()
                // 4.2 Le pasamos el path que tiene nuestro endpoint
                .uri("/game")
                // 4.3 Solo aceptaremos el formato JSON
                .accept(MediaType.APPLICATION_JSON)
                // 4.4 Usamos exchange para acceder al método expectStatus()
                .exchange()
                // 4.5 Esperamos que el status sea OK(200) = isOk()
                .expectStatus().isOk()
                // 4.6 Devolvemos el resultado y le pasamos la class
                .returnResult(BoardOutputDto.class)
                .getResponseBody();
    }

    @Test
    void testGetBoard(){
        // 1. Creamos nuestra salida
        Board board = new Board();
        // 1.1 Seteamos campos
        board.setId(1); board.setIdHostPlayer(1); board.setIdGuestPlayer(2); board.setNumColumns(7); board.setNumRows(6);
        board.setMatriz(new int[6][7]);
        // 1.2
        Mono<BoardOutputDto> boardMono = Mono.just(new BoardOutputDto(board));
        Mockito.when(gameService.findBoardById(anyInt())).thenReturn(boardMono);

        // 4. Usamos WebTestClient para probar el endpoint
        Flux<BoardOutputDto> fluxBoard = webTestClient
                // 4.1 Usamos el método GET
                .get()
                // 4.2 Le pasamos el path que tiene nuestro endpoint
                .uri("/game/1")
                // 4.3 Solo aceptaremos el formato JSON
                .accept(MediaType.APPLICATION_JSON)
                // 4.4 Usamos exchange para acceder al método expectStatus()
                .exchange()
                // 4.5 Esperamos que el status sea OK(200) = isOk()
                .expectStatus().isOk()
                // 4.6 Devolvemos el resultado y le pasamos la class
                .returnResult(BoardOutputDto.class)
                .getResponseBody();

        StepVerifier.create(fluxBoard)
                .expectSubscription()
                .expectNextMatches(b -> b.getIdHostPlayer() == 1)
                .verifyComplete();
    }

}
