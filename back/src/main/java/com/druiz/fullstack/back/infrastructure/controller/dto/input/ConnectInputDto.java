package com.druiz.fullstack.back.infrastructure.controller.dto.input;

import lombok.Data;

@Data
public class ConnectInputDto {

    PlayerInputDto player2;
    int gameId;

}