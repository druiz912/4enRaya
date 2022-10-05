package com.druiz.fullstack.back.infrastructure.controller.dto.input;

import com.druiz.fullstack.back.domain.FourConnect;
import com.druiz.fullstack.back.domain.GameStatus;
import lombok.Data;

@Data
public class GameInputDto {
    private String player1;
    private String player2;
    private GameStatus gameStatus;
    private int [][] board;
    private FourConnect winner;
}
