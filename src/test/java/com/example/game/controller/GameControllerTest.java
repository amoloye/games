package com.example.game.controller;

import static org.assertj.core.api.Assertions.assertThat;


import com.example.game.model.dto.GameDto;
import com.example.game.model.dto.GameRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;


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

    private final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    @AfterEach
    public void cleanup() {
        executor.shutdownNow();
    }

    @Test
    public void testPlayGame() throws InterruptedException {
        List<Callable<GameDto>> tasks = new ArrayList<>();
        for (int i = 0; i < NUM_GAMES; i++) {
            GameRequest request = new GameRequest(ThreadLocalRandom.current().nextInt(1, 101), ThreadLocalRandom.current().nextDouble(1, 100));
            tasks.add(() -> webClient.post()
                    .uri("/games")
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GameDto.class)
                    .returnResult()
                    .getResponseBody());
        }

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

        double totalBet = results.stream().mapToDouble(GameDto::getBet).sum();
        double totalWin = results.stream().mapToDouble(GameDto::getWin).sum();
        double rtp = (totalWin / totalBet) * 100;

        System.out.println("Total bet: " + totalBet);
        System.out.println("Total win: " + totalWin);
        System.out.println("RTP: " + rtp + "%");

        assertThat(rtp).isGreaterThan(99.0);
    }
}

