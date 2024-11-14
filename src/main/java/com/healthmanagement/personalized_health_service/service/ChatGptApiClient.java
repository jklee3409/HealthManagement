package com.healthmanagement.personalized_health_service.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Service
public class ChatGptApiClient {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    public ChatGptApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
    }

    public String getRecommendation(String prompt) {
        try {
            return this.webClient.post()
                    .uri("")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(createRequestBody(prompt))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to retrieve from chatGPT API", e);
        }
    }

    private Map<String, Object> createRequestBody(String prompt) {
        return Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 1000,
                "temperature", 0.7
        );
    }
}
