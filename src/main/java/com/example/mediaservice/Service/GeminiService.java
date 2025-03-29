package com.example.mediaservice.Service;

import com.example.mediaservice.Dto.Response.DiseasesResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyDrESTmYWs_7Yh8BrsdUIMZqgQqykCJoAc";


    private final WebClient webClient;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(GEMINI_API_URL).build();
    }

    public String getNextSymptomQuestion(String sessionId, List<DiseasesResponse> possibleDiseases) {
        String diseaseNames = possibleDiseases.stream()
                .map(DiseasesResponse::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String prompt = "Dựa trên các bệnh có thể mắc: " + diseaseNames +
                ", hãy đặt một câu hỏi để xác định bệnh chính xác hơn.Không đặt lại câu hỏi về các triệu chứng đã có. Chỉ hỏi về các triệu chứng chưa có để thu hẹp chẩn đoán";

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
