package com.druiz.fullstack.back.domain;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    private Integer id;
    private String user;
    private String ip;


    public Player(PlayerInputDto playerInputDto){
        setUser(playerInputDto.getName());
    }

}