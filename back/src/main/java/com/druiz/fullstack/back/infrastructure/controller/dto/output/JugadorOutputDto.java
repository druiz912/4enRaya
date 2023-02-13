package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import com.druiz.fullstack.back.domain.Board;
import com.druiz.fullstack.back.domain.Jugador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JugadorOutputDto {

    private int idPlayer;
    private String userPlayer;

    public JugadorOutputDto(Board board) {
        board.setIdHostPlayer(getIdPlayer());
        board.setIdGuestPlayer(getIdPlayer());
    }

    public JugadorOutputDto(Jugador jugador) {
        idPlayer = jugador.getId();
        userPlayer = jugador.getUserPlayer();
    }
}
