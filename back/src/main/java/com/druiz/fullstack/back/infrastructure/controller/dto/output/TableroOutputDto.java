package com.druiz.fullstack.back.infrastructure.controller.dto.output;

import com.druiz.fullstack.back.domain.Ficha;
import com.druiz.fullstack.back.domain.Jugador;
import com.druiz.fullstack.back.domain.Tablero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object para mostrar la información de una entidad Tablero.
 */
@Data
public class TableroOutputDto {
    private int id;
    private int filas;
    private int columnas;
    // Debemos inicializar la lista "fichas" para evitar un NullPointerException
    private List<FichaOutputDto> fichas;

    private List<JugadorOutputDto> jugadores = new ArrayList<>();

    /**
     * Crea una nueva instancia de {@link TableroOutputDto} a partir de una instancia de {@link Tablero}.
     * @param tablero la instancia de {@link Tablero} a partir de la cual se creará {@link TableroOutputDto}.
     */
    public TableroOutputDto(Tablero tablero) {
        this.id = tablero.getId();
        this.filas = tablero.getFilas();
        this.columnas = tablero.getColumnas();
        this.fichas = obtenerFichas(tablero);
    }

    /**
     * Crea una nueva instancia de {@link TableroOutputDto} a partir de una instancia de {@link Tablero} y {@link Jugador}.
     * @param tablero la instancia de {@link Tablero} a partir de la cual se creará {@link TableroOutputDto}.
     * @param jugador la instancia de {@link Jugador} a partir de la cual se creará {@link TableroOutputDto}.
     */
    public TableroOutputDto(Tablero tablero, Jugador jugador) {
        this.id = tablero.getId();
        this.filas = tablero.getFilas();
        this.columnas = tablero.getColumnas();
        this.fichas = obtenerFichas(tablero);
        this.jugadores.add(new JugadorOutputDto(jugador));
    }

    /**
     * Esta función obtiene una lista de fichas de un tablero y las devuelve en formato FichaOutputDto.
     *
     * @param tablero El tablero de donde obtener las fichas.
     * @return Una lista de objetos FichaOutputDto con los datos de las fichas.
     */
    public List<FichaOutputDto> obtenerFichas(Tablero tablero) {
        List<FichaOutputDto> listaFichas = new ArrayList<>();
        Ficha[][] matrizFichas = tablero.getFichas();


        // Recorremos la matriz de fichas para obtener cada una de ellas | i = filas | j = columnas
        for (int i = 0; i < matrizFichas.length; i++) {
            for (int j = 0; j < matrizFichas[i].length; j++) {
                Ficha ficha = matrizFichas[i][j];
                // Si la ficha es distinta de null, añadimos los datos de la ficha a la lista de FichaOutputDto
                if (ficha != null) {
                    listaFichas.add(new FichaOutputDto(
                            ficha.getId(),
                            ficha.getJugador().getId(),
                            i,
                            j));
                }
            }
        }

        return listaFichas;
    }

    /**
     * Esta función obtiene una lista de jugadores de un tablero y los devuelve en formato JugadorOutputDto.
     *
     * @param tablero El tablero de donde obtener los jugadores.
     * @return Una lista de objetos JugadorOutputDto con los datos de los jugadores.
     */
    public void obtenerJugadores(Tablero tablero) {
        this.jugadores = tablero.getJugadores().stream()
                .map(jugador -> new JugadorOutputDto(jugador.getId(),
                        jugador.getUserPlayer()))
                .toList();
    }
}

