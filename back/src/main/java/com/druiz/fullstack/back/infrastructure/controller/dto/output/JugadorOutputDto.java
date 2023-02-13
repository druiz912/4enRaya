package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.Jugador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerOutputDto {

    private int idPlayer;
    private String userPlayer;

    public PlayerOutputDto(Board board) {
        board.setIdHostPlayer(getIdPlayer());
        board.setIdGuestPlayer(getIdPlayer());
    }

    public PlayerOutputDto(Jugador jugador) {
        idPlayer = jugador.getId();
        userPlayer = jugador.getUserPlayer();
    }
}
