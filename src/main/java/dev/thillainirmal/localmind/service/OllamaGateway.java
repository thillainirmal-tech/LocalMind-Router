package dev.thillainirmal.localmind.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class OllamaGateway {
    private final RestClient client;

    public OllamaGateway(RestClient.Builder builder,
                         @Value("${localmind.ollama.base-url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @SuppressWarnings("unchecked")
    public String chat(String model, String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "stream", false,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "options", Map.of("temperature", 0.2));

        Map<String, Object> response = client.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        if (response == null || !(response.get("message") instanceof Map<?, ?> message)) {
            throw new IllegalStateException("Ollama returned an invalid response");
        }
        Object content = message.get("content");
        if (!(content instanceof String text) || text.isBlank()) {
            throw new IllegalStateException("Ollama returned an empty response");
        }
        return text;
    }
}
