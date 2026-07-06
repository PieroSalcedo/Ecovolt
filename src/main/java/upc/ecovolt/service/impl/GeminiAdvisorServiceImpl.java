package upc.ecovolt.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import upc.ecovolt.mapping.dto.EnergyAdvisorDto;
import upc.ecovolt.service.GeminiAdvisorService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiAdvisorServiceImpl implements GeminiAdvisorService {

    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-3.1-flash-lite}")
    private String model;

    @Override
    public Optional<EnergyAdvisorDto.GeminiPayload> generateAdvice(String context, List<EnergyAdvisorDto.Metric> metrics) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("SMART ADVISOR: GEMINI_API_KEY no configurada. Se usaran recomendaciones por reglas.");
            return Optional.empty();
        }

        try {
            String prompt = buildPrompt(context, metrics);
            ObjectNode body = buildGeminiBody(prompt);
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.warn("SMART ADVISOR: Gemini respondio con status {}", response.statusCode());
                return Optional.empty();
            }

            String text = extractGeminiText(response.body());
            String json = sanitizeJson(text);
            return Optional.of(objectMapper.readValue(json, EnergyAdvisorDto.GeminiPayload.class));
        } catch (Exception ex) {
            log.warn("SMART ADVISOR: No se pudo generar recomendacion IA. Motivo: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    private ObjectNode buildGeminiBody(String prompt) {
        ObjectNode body = objectMapper.createObjectNode();
        ArrayNode contents = body.putArray("contents");
        ObjectNode content = contents.addObject();
        ArrayNode parts = content.putArray("parts");
        parts.addObject().put("text", prompt);

        ObjectNode config = body.putObject("generationConfig");
        config.put("temperature", 0.35);
        config.put("responseMimeType", "application/json");

        return body;
    }

    private String buildPrompt(String context, List<EnergyAdvisorDto.Metric> metrics) {
        return """
                Eres EcoVolt Smart Advisor, un asesor de eficiencia energetica para hogares IoT.
                No inventes numeros. Usa solo el contexto y metricas entregadas por el backend.
                Devuelve exclusivamente un JSON valido con esta estructura:
                {
                  "summary": "texto breve",
                  "riskLevel": "LOW | MEDIUM | HIGH",
                  "estimatedSaving": 0.00,
                  "recommendations": [
                    {
                      "title": "texto",
                      "description": "texto",
                      "priority": "LOW | MEDIUM | HIGH",
                      "category": "AHORRO | ALERTA | META | DISPOSITIVO"
                    }
                  ]
                }
                Contexto calculado por Spring Boot:
                %s
                Metricas:
                %s
                """.formatted(context, metrics);
    }

    private String extractGeminiText(String body) throws Exception {
        JsonNode root = objectMapper.readTree(body);
        JsonNode textNode = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");

        if (textNode.isMissingNode() || textNode.asText().isBlank()) {
            throw new IllegalStateException("Respuesta de Gemini sin texto.");
        }

        return textNode.asText();
    }

    private String sanitizeJson(String text) {
        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
