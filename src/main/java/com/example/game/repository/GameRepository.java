package com.example.game.repository;

import com.example.game.model.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, String> {
    Mono<Game> findByPlayerNumber(int playerNumber);
}
