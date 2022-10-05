package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.config.exceptions.NotFoundException;
import com.druiz.fullstack.back.config.exceptions.UnprocessableException;
import com.druiz.fullstack.back.domain.*;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.GameOutputDto;
import com.druiz.fullstack.back.infrastructure.repo.GameRepo;
import com.druiz.fullstack.back.infrastructure.repo.PlayersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private PlayersRepo playersRepo;

    public Mono<GameOutputDto> createGame(PlayerInputDto player) {
        // 1. Instanciamos Entidad Game
        Game game = new Game();
        // 2. Devolvemos game envuelto (Mono.just)
        return Mono.just(game)
                // 3. flatMap mapeamos
                .flatMap(g -> {
                    g.setBoard(new int[7][6]);
                    g.setPlayer1(player.getName());
                    g.setGameStatus(GameStatus.NEW);
                    return gameRepo.save(g);
                })
                .flatMap(req -> playersRepo.save(new Player(player)))
                .map(p -> new GameOutputDto(new Game(player)));

    }

    public Flux<Game> getGames() {
        return gameRepo.findAll();
    }

    public Mono<Object> connectToGame(PlayerInputDto player2, int gameId) {
        return gameRepo.findById(gameId)
                .switchIfEmpty(Mono.error(new NotFoundException("Partida con id: " + gameId + " not found")))
                .flatMap(g -> {
                    g.setPlayer2(player2.getName());
                    g.setGameStatus(GameStatus.IN_PROGRESS);
                    return gameRepo.save(g);
                })
                .flatMap(p -> playersRepo.findByName(player2.getName())
                        .switchIfEmpty(playersRepo.save(new Player(player2))));
    }

    public Mono<Game> gameplay(GamePlay gamePlay) {

        return gameRepo.findById(gamePlay.getGameId())
                .switchIfEmpty(Mono.error(new NotFoundException("La partida no existe")))
                .flatMap(g -> {
                    if (g.getGameStatus().equals(GameStatus.NEW) || g.getGameStatus().equals(GameStatus.IN_PROGRESS)) {

                        int[][] boardGame = g.getBoard();
                        int counterIndex = 0;
                        for (int i = 0; i < 7; i++) {
                            for (int j = 0; j < 6; j++) {

                                if (boardGame[gamePlay.getColumn()][j] == 0) {
                                    counterIndex = j;
                                }

                            }
                        }
                        boardGame[gamePlay.getColumn()][counterIndex] = gamePlay.getType().getValue();

                        boolean redWinner = false;
                        boolean greenWinner = false;

                        //Checking winner on vertical lines
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 7; j++) {
                                if (boardGame[j][i] == boardGame[j][i + 1] && (boardGame[j][i] == boardGame[j][i + 2]) && (boardGame[j][i + 3] == boardGame[j][i])) {
                                    if (boardGame[j][i] == FourConnect.WHITE.getValue() || boardGame[j][i] == FourConnect.BLACK.getValue()) {
                                        if (boardGame[j][i] == FourConnect.WHITE.getValue()) {
                                            greenWinner = true;
                                            g.setWinner(FourConnect.WHITE);
                                            g.setGameStatus(GameStatus.FINISHED);
                                            gameRepo.save(g);
                                        }
                                        if (boardGame[j][i] == FourConnect.BLACK.getValue()) {
                                            redWinner = true;
                                            g.setWinner(FourConnect.BLACK);
                                            g.setGameStatus(GameStatus.FINISHED);
                                            gameRepo.save(g);
                                        }
                                    }
                                }
                            }

                        }

                        //Check winner on horizontal lines
                        for (int i = 0; i < 6; i++) {
                            for (int j = 0; j < 4; j++) {
                                if (boardGame[j][i] == boardGame[j + 1][i] && (boardGame[j][i] == boardGame[j + 2][i]) && (boardGame[j + 3][i] == boardGame[j][i])) {
                                    if (boardGame[j][i] == FourConnect.WHITE.getValue() || boardGame[j][i] == FourConnect.BLACK.getValue()) {
                                        if (boardGame[j][i] == FourConnect.WHITE.getValue()) {
                                            greenWinner = true;
                                            g.setWinner(FourConnect.WHITE);
                                            g.setGameStatus(GameStatus.FINISHED);
                                            gameRepo.save(g);
                                        }
                                        if (boardGame[j][i] == FourConnect.BLACK.getValue()) {
                                            redWinner = true;
                                            g.setWinner(FourConnect.BLACK);
                                            g.setGameStatus(GameStatus.FINISHED);
                                            gameRepo.save(g);
                                        }
                                    }
                                }
                            }

                        }

                        //Check winner on left diagonal lines
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 4; j++) {
                                if (boardGame[j][i] == boardGame[j + 1][i + 1] && (boardGame[j][i] == boardGame[j + 2][i + 2]) && (boardGame[j][i] == boardGame[j + 3][i + 3])) {
                                    if (boardGame[j][i] == FourConnect.WHITE.getValue() || boardGame[j][i] == FourConnect.BLACK.getValue()) {
                                        if (boardGame[j][i] == FourConnect.WHITE.getValue()) {
                                            greenWinner = true;
                                            g.setWinner(FourConnect.WHITE);
                                            g.setGameStatus(GameStatus.FINISHED);
                                            gameRepo.save(g);
                                        }
                                        if (boardGame[j][i] == FourConnect.BLACK.getValue()) {
                                            redWinner = true;
                                            g.setWinner(FourConnect.BLACK);
                                            g.setGameStatus(GameStatus.FINISHED);
                                            gameRepo.save(g);
                                        }
                                    }
                                }
                            }

                        }


                        //Check right diagonal winner

                        System.out.println(redWinner);
                        System.out.println(greenWinner);

                        return gameRepo.save(g);

                    } else {
                        throw new UnprocessableException("Partida acabada");
                    }
                })

                ;

    }


}