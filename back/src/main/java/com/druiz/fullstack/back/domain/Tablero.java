package com.druiz.fullstack.back.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

@Table(name = "boards")
@Data
@NoArgsConstructor
/**
 * Representa un tablero de juego de 4 en raya.
 */
public class Tablero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int filas;
    private int columnas;
    private List<Ficha> fichas;

    /**
     * Crea un nuevo tablero de juego de 4 en raya.
     *
     * @param filas el número de filas del tablero
     * @param columnas el número de columnas del tablero
     */
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.fichas = new ArrayList<>();
    }

    /**
     * Devuelve el número de filas del tablero.
     *
     * @return el número de filas del tablero
     */
    public int getFilas() {
        return filas;
    }

    /**
     * Devuelve el número de columnas del tablero.
     *
     * @return el número de columnas del tablero
     */
    public int getColumnas() {
        return columnas;
    }

    /**
     * Devuelve la lista de fichas colocadas en el tablero.
     *
     * @return la lista de fichas colocadas en el tablero
     */
    public List<Ficha> getFichas() {
        return fichas;
    }

    /**
     * Agrega una nueva ficha al tablero.
     *
     * @param ficha la ficha a agregar
     */
    public void agregarFicha(Ficha ficha) {
        fichas.add(ficha);
    }
}
