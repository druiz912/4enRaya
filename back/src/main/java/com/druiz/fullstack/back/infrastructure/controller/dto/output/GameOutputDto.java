package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import com.druiz.fullstack.back.domain.FourConnect;
import com.druiz.fullstack.back.domain.Game;
import com.druiz.fullstack.back.domain.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameOutputDto {
    private int id;
    private String player1;
    private String player2;
    private GameStatus gameStatus;
    private int [][] board;
    private FourConnect winner;

    public GameOutputDto(Game game){
        setId(game.getId());
        setPlayer1(game.getPlayer1());
        setPlayer2(game.getPlayer2());
        setGameStatus(game.getGameStatus());
        setBoard(game.getBoard());
        setWinner(game.getWinner());
    }

}
