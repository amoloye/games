package com.example.game.model.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRequest {

    @Min(1)
    @Max(100)
    @NotNull(message = "Please input a number between 1 and 100 ")
    private int playerNumber;

    @Positive
    @NotNull(message = "please specify an amount to play bet")
    private double bet;

}
