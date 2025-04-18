package com.example.mediaservice.Service;

import com.example.mediaservice.Entity.QuestionAnswer;
import org.json.JSONObject;
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



    public String getNextSymptomQuestion(String sessionId, List<QuestionAnswer> previousQuestionsAndAnswers,boolean check ,String Message) {
        StringBuilder prompt = new StringBuilder("Dựa trên các câu hỏi và câu trả lời trước đó, hãy đặt một câu hỏi mới để thu hẹp chẩn đoán.\n");

        if(check){
            for (QuestionAnswer qa : previousQuestionsAndAnswers) {
                prompt.append("Câu hỏi: ").append(qa.getQuestion()).append("\n");
                prompt.append("Câu trả lời: ").append(qa.getAnswer()).append("\n");
            }
        }
        else{
            prompt.append("Triêu chứng: ").append(Message);
        }
        // Xây dựng phần dữ liệu câu hỏi và câu trả lời


        prompt.append("Hãy tạo câu hỏi tiếp theo để xác định bệnh chính xác hơn. không giải thích dài dòng chỉ đưa ra câu hỏi liên quan để xac định bệnh chính xac hơn");

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", prompt.toString())
                        })
                }
        );

        // Gọi API và nhận câu hỏi từ Gemini
        String responseJson  = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // Phân tích kết quả trả về từ Gemini

        // Giả sử kết quả trả về là một câu hỏi, bạn cần phân tích nó để trích xuất câu hỏi thực tế
        JSONObject response = new JSONObject(responseJson);

        // Lấy câu hỏi từ Gemini (câu text)
        String questionText = response.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        // In ra câu hỏi (text) mà Gemini đã trả về
        System.out.println(questionText);
        return questionText;
    }
    public String getMyAnwer(String sessionId, List<QuestionAnswer> previousQuestionsAndAnswers) {
        StringBuilder prompt = new StringBuilder("Dựa trên các câu hỏi và câu trả lời trước đó, hãy chuẩn đoán bệnh có thể mắc phải.\n");

        for (QuestionAnswer qa : previousQuestionsAndAnswers) {
            prompt.append("Câu hỏi: ").append(qa.getQuestion()).append("\n");
            prompt.append("Câu trả lời: ").append(qa.getAnswer()).append("\n");
        }


        // Xây dựng phần dữ liệu câu hỏi và câu trả lời


        prompt.append("Hãy đưa ra phương pháp trị bênh tốt nhất.");

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", prompt.toString())
                        })
                }
        );

        // Gọi API và nhận câu hỏi từ Gemini
        String responseJson  = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // Phân tích kết quả trả về từ Gemini

        // Giả sử kết quả trả về là một câu hỏi, bạn cần phân tích nó để trích xuất câu hỏi thực tế
        JSONObject response = new JSONObject(responseJson);

        // Lấy câu hỏi từ Gemini (câu text)
        String questionText = response.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        // In ra câu hỏi (text) mà Gemini đã trả về
        System.out.println(questionText);
        return questionText;
    }
    private String extractQuestionFromResponse(String response) {
        return response.trim();  // Giả sử chỉ trả về câu hỏi duy nhất
    }
}
