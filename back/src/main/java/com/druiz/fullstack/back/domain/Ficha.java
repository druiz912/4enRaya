package com.druiz.fullstack.back.domain;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Representa una ficha en el tablero de juego de 4 en raya.
 */
@Entity
@NoArgsConstructor
public class Ficha {
    @Id
    private String id;
    private Jugador jugador;
    private int fila;
    private int columna;

    /**
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
     * Devuelve el identificador único de la ficha.
     * @return el identificador único de la ficha
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único de la ficha.
     * @param id el identificador único de la ficha
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el jugador que colocó la ficha.
     * @return el jugador que colocó la ficha
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Establece el jugador que colocó la ficha.
     *
     * @param jugador el jugador que colocó la ficha
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Devuelve la fila en la que se colocó la ficha.
     * @return la fila en la que se colocó la ficha
     */
    public int getFila() {
        return fila;
    }

    /**
     * Establece la fila en la que se colocó la ficha.
     *
     * @param fila la fila en la que se colocó la ficha
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Devuelve la columna en la que se colocó la ficha.
     *
     * @return la columna en la que se colocó la ficha
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Establece la columna en la que se colocó la ficha.
     *
     * @param columna la columna en la que se colocó la ficha
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }
}

