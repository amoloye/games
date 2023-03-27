package com.example.game.model.dto;


import lombok.*;

@Data
@Builder
public class GameDto {

    private String gameId;
    private int playerNumber;
    private double bet;
    private int randomNumber;
    private double win;




}

