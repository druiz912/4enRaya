package com.druiz.fullstack.back.config.mappers;

import com.druiz.fullstack.back.domain.Jugador;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    Jugador toEntity(PlayerInputDto playerInputDto);

}
