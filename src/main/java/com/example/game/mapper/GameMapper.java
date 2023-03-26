package com.example.game.mapper;


import com.example.game.model.Game;
import com.example.game.model.dto.GameDto;
import com.example.game.model.dto.GameRequest;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    Game toEntity(GameRequest gameRequest);

    GameDto toDto(Game game);

    default Mono<GameDto> toDtoMono(Mono<Game> game) {
        return game.map(this::toDto).cast(GameDto.class);
    }
}



