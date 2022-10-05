package com.druiz.fullstack.back.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FourConnect {
    BLACK(1), WHITE(2), EMPTY(0);

    private final Integer value;
}