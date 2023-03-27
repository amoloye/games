package com.example.game.mapper;


import com.example.game.model.Game;
import com.example.game.model.dto.GameDto;

import org.springframework.beans.BeanUtils;


public class GameMapper {

    public GameDto entityToDto (Game game){
        GameDto gameDto = new GameDto();
        BeanUtils.copyProperties(game, gameDto);
        return  gameDto;

    }

    public static Game dtoToEntity (GameDto gameDto){
        Game game = new Game();
        BeanUtils.copyProperties(gameDto,game);
        return  game;

    }
}



