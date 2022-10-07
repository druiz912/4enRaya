package com.druiz.fullstack.back.config.mappers;

import com.druiz.fullstack.back.infrastructure.controller.dto.input.PlayerInputDto;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.BoardOutputDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface BoardMapper {
    BoardOutputDto fromDtoPlayerToDtoBoard(PlayerInputDto playerInputDto);

}
