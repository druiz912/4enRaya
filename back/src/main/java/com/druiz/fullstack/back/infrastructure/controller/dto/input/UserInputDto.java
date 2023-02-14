package com.druiz.fullstack.back.infrastructure.controller.dto.input;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDto {

     /**
      * Nombre de usuario.
      */
     private String username;

     /**
      * Contraseña del usuario.
      */
     private String password;
}
