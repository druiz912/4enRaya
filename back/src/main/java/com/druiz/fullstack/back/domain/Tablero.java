package com.druiz.fullstack.back.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Table(name = "boards")
@Data
@Builder
@NoArgsConstructor
public class Board {

    @Id
    private int id;
    // Número de filas
    @NotNull
    private int numRows;
    // Número de columnas
    @NotNull
    private int numColumns;
    @NotNull
    private int idHostPlayer;
    @NotNull
    private int idGuestPlayer;
    @NotNull
    private int[][] matriz;

    private GameStatus status;


    public Board(int id, int numRows, int numColumns, int idHostPlayer, int idGuestPlayer, @NotNull int[][] matriz, GameStatus status) {
        this.id = id;
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.idHostPlayer = idHostPlayer;
        this.idGuestPlayer = idGuestPlayer;
        this.matriz = matriz;
        this.status = status;
    }
}
