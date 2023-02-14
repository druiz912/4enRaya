package com.druiz.fullstack.back.infrastructure.controller.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Data Transfer Object para crear una entidad Jugador.
 * @Data (Lombok) y genera automáticamente los métodos get, set, equals, hashCode, etc.
 * @AllArgsConstructor genera constructores completos.
 * @NoArgsConstructor generan constructores vacíos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JugadorInputDto {

    @NotNull
    private String userPlayer;

    @Min(value = 1)
    private Integer idUser;
}