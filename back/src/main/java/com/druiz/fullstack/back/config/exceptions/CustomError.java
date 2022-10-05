package com.druiz.fullstack.back.config.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {
    private int httpCode;
    private String message;
    private Date date;
}
