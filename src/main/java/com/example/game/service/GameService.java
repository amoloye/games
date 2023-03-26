package com.example.game.service;

import com.example.game.mapper.GameMapper;
import com.example.game.model.Game;
import com.example.game.model.dto.GameDto;
import com.example.game.model.dto.GameRequest;
import com.example.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper = GameMapper.INSTANCE;

    public Mono<ResponseEntity<GameDto>> playGame(GameRequest gameRequest) {
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 101);
        double win = 0;
        if (gameRequest.getPlayerNumber() >= randomNumber) {
            win = gameRequest.getBet() * 1.95;
        }
        Game game = Game.builder()
                .playerNumber(gameRequest.getPlayerNumber())
                .bet(gameRequest.getBet())
                .randomNumber(randomNumber)
                .win(win)
                .build();
        return gameRepository.save(game)
                .map(gameMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<GameDto>> findGameByPlayerNumber(int playerNumber) {
        return gameRepository.findByPlayerNumber(playerNumber)
                .map(gameMapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}


