package com.druiz.fullstack.back.infrastructure.controller.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDto {
    private Integer idPlayer;
    private Integer idBoard;
    private Integer column;
    private Integer value;
}
