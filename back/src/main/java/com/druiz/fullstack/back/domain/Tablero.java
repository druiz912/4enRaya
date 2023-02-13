package com.druiz.fullstack.back.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un tablero de juego de 4 en raya.
 */
@Table(name = "Tableros")
@Data
@NoArgsConstructor
public class Tablero {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int filas;
    private int columnas;
    private Ficha[][] fichas;

    // Debemos inicializar la lista "jugadores" para evitar un NullPointerException
    private List<Jugador> jugadores = new ArrayList<>();

    private String status;

    /**
     * Crea un nuevo tablero de juego de 4 en raya.
     *
     * @param filas el número de filas del tablero
     * @param columnas el número de columnas del tablero
     */
    public Tablero(int filas, int columnas, List<Jugador> jugadores) {
        this.filas = filas;
        this.columnas = columnas;
        this.fichas = new Ficha[filas][columnas];
        if (this.jugadores.size() == 1) {
            this.status = "Game not started";
        } else if (this.jugadores.size() == 2) {
            this.status = "Game completed! but not finished";
        }
        this.jugadores = jugadores;
    }

    /**
     * Establece las fichas en el tablero inicializando una matriz de Ficha.
     */
    public void setFichas() {
        this.fichas = new Ficha[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                this.fichas[i][j] = new Ficha();
            }
        }
    }


    /**
     * Añade un jugador al tablero si el ID del jugador no es nula.
     *
     * @param jugador El jugador a añadir.
     */
    public void addJugador(Jugador jugador){
        if (jugador.getId() != null){
            this.jugadores.add(jugador);
        }
    }

    /**
     * Elimina un jugador del tablero si el ID del jugador no es nula.
     *
     * @param jugador El jugador a eliminar.
     */
    protected void removeJugador(Jugador jugador){
        if (jugador.getId() != null){
            this.jugadores.remove(jugador);
        }
    }

}
