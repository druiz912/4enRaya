package com.druiz.fullstack.front.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class Board {

    private Integer id;
    private String player1;
    private String player2;

    private int numRows;

    private int numColumns;

    private int[][] matriz;
    private String status;

    public Board(Board board) {
        id = board.getId();
        player1 = board.getPlayer1();
        player2 = board.getPlayer2();
        numRows = board.getNumRows();
        numColumns = board.getNumColumns();
        matriz = board.getMatriz();
        status = board.getStatus();
    }
}
