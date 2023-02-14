package com.druiz.fullstack.back.infrastructure.controller.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaInputDto {

    @NotNull
    private Integer idJugador;
    @NotNull
    private Integer idTablero;
    @NotNull
    private Integer columna;
    @NotNull
    private Integer fila;
    @NotNull
    private Integer value;
}
