package com.healthmanagement.personalized_health_service.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ChatGptApiClient {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    public ChatGptApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/completions").build();
    }

    public String getRecommendation(String prompt) {
        try {
            return this.webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(createRequestBody(prompt))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to retrieve from chatGPT API", e);
        }
    }

    private Map<String, Object> createRequestBody(String prompt) {
        return Map.of(
                "model", "text-davinci-003",
                "prompt", prompt,
                "max_tokens", 150,
                "temperature", 0.7
        );
    }
}
