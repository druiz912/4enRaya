package com.druiz.fullstack.back.domain;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.JugadorInputDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Table(name = "jugadores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jugador {

    @Id
    private Integer id;

    @NotNull
    private String userPlayer;

    /**
     * Relación 1:1 con la entidad User.
     * La entidad Jugador tiene una referencia a un objeto de la entidad User.
     * @Reference Tiene dos campos:
     *  1. El campo value especifica la entidad a la que se refiere la relación.
     *  2. El campo to especifica la clase a la que va en la entidad actual (Jugador), se usará para la relación
     */
    /*
    @Reference(
            value = User.class,
            to = Jugador.class
    )
    private User user;
    */


    public Jugador(int id, String userPlayer){
        this.id = id;
        this.userPlayer = userPlayer;
    }

    public Jugador(JugadorInputDto dto) {
        this.userPlayer =  dto.getUserPlayer();
    }

   public Jugador(Jugador jugador) {
        this.id = jugador.getId();
        this.userPlayer = jugador.getUserPlayer();
   }
}