package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardOutputDto {

    private int idBoard;
    private int numRows;
    private int numColumns;
    private int idHostPlayer;
    private int idGuestPlayer;
    private int [][] matriz;
    private GameStatus status;


    public BoardOutputDto(Board board) {
        idBoard = board.getId();
        numRows = board.getNumRows();
        numColumns = board.getNumColumns();
        idHostPlayer = board.getIdHostPlayer();
        idGuestPlayer = board.getIdGuestPlayer();
        matriz = board.getMatriz();
        status = board.getStatus();
    }
}
