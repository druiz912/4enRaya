package com.druiz.fullstack.back.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import javax.persistence.Table;

/**
 * Representa una ficha en el tablero de juego de 4 en raya.
 */
@Table
@Data
@NoArgsConstructor
public class Ficha {
    @Id
    private Integer id;
    private Jugador jugador;
    private int fila;
    private int columna;

    private boolean disponible;

    /**
     * MOVIMIENTO
     * Crea una nueva ficha en el tablero de juego de 4 en raya.
     *
     * @param jugador el jugador que colocó la ficha
     * @param fila    la fila en la que se colocó la ficha
     * @param columna la columna en la que se colocó la ficha
     */
    public Ficha(Jugador jugador, int fila, int columna) {
        this.jugador = jugador;
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Crea una nueva ficha en el tablero de juego de 4 en raya.
     *
     * @param fila    la fila en la que se colocó la ficha
     * @param columna la columna en la que se colocó la ficha
     */
    public Ficha(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Comprueba si la ficha está disponible para ser colocada.
     * Comparo si el atributo jugador es igual a null.
     *
     * @return `Si es null, significa que la ficha está disponible para ser colocada, y retorna true.
     * De lo contrario, se retorna 'false'
     */
    public boolean isDisponible() {
        return jugador == null;
    }

}

