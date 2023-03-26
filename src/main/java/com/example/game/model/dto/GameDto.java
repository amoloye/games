package com.example.game.model.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class GameDto {
    @Id
    private String gameId;
    private int playerNumber;
    private double bet;
    private int randomNumber;
    private double win;


}

