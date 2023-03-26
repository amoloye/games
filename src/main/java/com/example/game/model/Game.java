package com.example.game.model;


import com.example.game.model.dto.GameRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@Document(collection = "game")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Game {

    @Id
    private String gameId;

    private int playerNumber;
    private double bet;
    private int randomNumber;
    private double win;

    public Game(int randomNumber, double win, GameRequest gameRequest) {
        this.playerNumber = gameRequest.getPlayerNumber();
        this.bet = gameRequest.getBet();
        this.randomNumber = randomNumber;
        this.win = win;
    }

    @Builder
    public Game(String gameId, int playerNumber, double bet, int randomNumber, double win) {
        this.gameId = gameId;
        this.playerNumber = playerNumber;
        this.bet = bet;
        this.randomNumber = randomNumber;
        this.win = win;
    }
}









