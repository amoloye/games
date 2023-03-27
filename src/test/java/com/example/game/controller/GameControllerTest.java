package com.example.game.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static reactor.core.publisher.Mono.when;


import com.example.game.model.dto.GameDto;
import com.example.game.model.dto.GameRequest;
import com.example.game.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest {

    private static final int NUM_GAMES = 1_000_000;
    private static final int NUM_THREADS = 24;

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GameService gameService;

    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    @AfterEach
    public void cleanup () {
        executor.shutdownNow();
    }

    @Test
    public void testPlayGame () throws InterruptedException {
        // Mock the game service to return a fixed GameDto
        when(gameService.playGame(any())).thenReturn(Mono.just(GameDto.builder()
                .playerNumber(1)
                .bet(10.0)
                .win(1.0)
                .build()));

        // Create a list of tasks to execute concurrently
        List<Callable<GameDto>> tasks = new ArrayList<>();
        for (int i = 0; i < NUM_GAMES; i++) {
            GameRequest request = GameRequest.builder()
                    .playerNumber(ThreadLocalRandom.current().nextInt(1, 101))
                    .bet(ThreadLocalRandom.current().nextDouble(1, 100))
                    .build();
            tasks.add(() -> webClient.post()
                    .uri("/games")
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GameDto.class)
                    .returnResult()
                    .getResponseBody());
        }

        // Execute the tasks and collect the results
        List<GameDto> results = executor.invokeAll(tasks)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // Calculate and print the RTP
        double totalBet = results.stream().mapToDouble(GameDto::getBet).sum();
        double totalWin = results.stream().mapToDouble(GameDto::getWin).sum();
        double rtp = (totalWin / totalBet) * 100;
        System.out.println("Total bet: " + totalBet);
        System.out.println("Total win: " + totalWin);
        System.out.println("RTP: " + rtp);
    }

    @Test
    public void testFindGameByPlayerNumber () {
        // Mock the game service to return a fixed GameDto
        when(gameService.findGameByPlayerNumber(eq(1))).thenReturn(Mono.just(GameDto.builder()
                .gameId("gameId")
                .playerNumber(1)
                .bet(10.0)
                .randomNumber(2)
                .win(1.0)
                .build()));

        // Send a GET request to the endpoint and assert the response body
        webClient.get()
                .uri("/games/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(GameDto.class)
                .isEqualTo(GameDto.builder()
                        .gameId("gameId")
                        .playerNumber(1)
                        .bet(10.0)
                        .randomNumber(2)
                        .win(1.0)
                        .build());
    }

}


