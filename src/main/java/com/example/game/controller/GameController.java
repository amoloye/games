package com.example.game.controller;


import com.example.game.model.dto.GameDto;
import com.example.game.model.dto.GameRequest;
import com.example.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/games")
    public Mono<ResponseEntity<ResponseEntity<GameDto>>> playGame(@RequestBody GameRequest gameRequest) {
        return gameService.playGame(gameRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/games/{playerNumber}")
    public Mono<ResponseEntity<ResponseEntity<GameDto>>> findGameByPlayerNumber(@PathVariable int playerNumber) {
        return gameService.findGameByPlayerNumber(playerNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/error")
    public String error() {
        return "An error has occurred";
    }

}









