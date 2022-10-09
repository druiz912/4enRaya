package com.druiz.fullstack.front.domain;

import lombok.Data;

@Data
public class Board {
    private int id;
    // Número de filas
    private int rows;
    // Número de columnas
    private int columns;
    private int idHostPlayer;
    private int idGuestPlayer;
    private int[][] matriz;

}
