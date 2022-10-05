package com.druiz.fullstack.back.domain;

import lombok.Data;

@Data
public class GamePlay {

    private FourConnect type;
    private int column;
    private int gameId;

}
