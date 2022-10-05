package com.druiz.fullstack.back.domain;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Table(name = "game")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    private int id;
    private String player1;
    private String player2;
    private GameStatus gameStatus;
    private int[][] board;
    private FourConnect winner;

    public Game(PlayerInputDto player) {
        setPlayer1(player.getName());
        // Aún no se ha conectado el player2
        setPlayer2(null);
        setGameStatus(GameStatus.NEW);
        // Tablero -> Matriz 3 | 3 -> 4 filas , 4 columnas
        setBoard(new int[3][3]);
        setWinner(null);
    }

    public GameStatus gameStatus(){ return gameStatus; }

}
