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
@AllArgsConstructor
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

    // Falta añadir si hay ganador


}
