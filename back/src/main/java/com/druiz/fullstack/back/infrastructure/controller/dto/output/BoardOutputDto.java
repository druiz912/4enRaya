package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import com.druiz.fullstack.back.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardOutputDto {

    private int idBoard;
    private int rows;
    private int columns;
    private int idHostPlayer;
    private int idGuestPlayer;
    private int [][] matriz;

    public BoardOutputDto(Board board) {
        idBoard = board.getId();
        rows = board.getRows();
        columns = board.getColumns();
        idHostPlayer = board.getIdHostPlayer();
        idGuestPlayer = board.getIdGuestPlayer();
        matriz = board.getMatriz();
    }
}
