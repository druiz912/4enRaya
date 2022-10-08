package com.druiz.fullstack.back.domain;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Table(name = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    private Integer id;

    @NotNull
    private String user;


    public Player(PlayerInputDto playerInputDto) {
        user =  playerInputDto.getUser();
    }
}