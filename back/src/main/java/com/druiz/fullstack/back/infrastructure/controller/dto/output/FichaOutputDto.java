package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import lombok.Data;

/**
 * DTO para mostrar la información de una ficha en el tablero de juego de 4 en raya.
 */
@Data
public class FichaOutputDto {
    private Integer id;
    private Integer idJugador;
    private int fila;
    private int columna;

    /**
     * Crea un nuevo DTO para mostrar la información de una ficha en el tablero de juego de 4 en raya.
     *
     * @param id       el id de la ficha
     * @param idJugador el id del jugador que colocó la ficha
     * @param fila    la fila en la que se colocó la ficha
     * @param columna la columna en la que se colocó la ficha
     */
    public FichaOutputDto(Integer id, Integer idJugador, int fila, int columna) {
        this.id = id;
        this.idJugador = idJugador;
        this.fila = fila;
        this.columna = columna;
    }
}
