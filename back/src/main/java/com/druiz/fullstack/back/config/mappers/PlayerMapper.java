package com.druiz.fullstack.back.config.mappers;

import com.druiz.fullstack.back.domain.Player;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface PlayerMapper
{

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @Mapping(target = "id", ignore = true)
    Player toEntity(PlayerInputDto playerInputDto);

}
