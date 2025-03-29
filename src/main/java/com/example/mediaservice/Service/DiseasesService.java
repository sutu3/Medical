package com.example.mediaservice.Service;

import com.example.mediaservice.Dto.Response.DiagnosisResponse;
import com.example.mediaservice.Dto.Response.DiseasesResponse;
import com.example.mediaservice.Entity.Diseases;
import com.example.mediaservice.Entity.PatientSymptom;
import com.example.mediaservice.Mapper.DiseasesMapper;
import com.example.mediaservice.Repo.DiseasesRepo;
import com.example.mediaservice.Repo.PatientSymptomRepo;
import com.example.mediaservice.Repo.SymptomsRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class DiseasesService {
    DiseasesRepo diseasesRepo;
    GeminiService geminiService;
    SymptomsRepo symptomsRepo;
    DiseasesMapper diseasesMapper;
    PatientSymptomRepo patientSymptomRepo;
    String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyDrESTmYWs_7Yh8BrsdUIMZqgQqykCJoAc";
    private final WebClient.Builder webClientBuilder;
    private final ClientHttpConnector webClientHttpConnector;


    public String diagnose(String sessionId, List<String> symptoms) {
        // 🟢 Lấy danh sách triệu chứng hợp lệ từ DB
        Set<String> validSymptoms = symptomsRepo.findAll().stream()
                .map(symptom -> symptom.getName().toLowerCase())
                .collect(Collectors.toSet());

        // 🛑 Lọc triệu chứng không hợp lệ
        List<String> invalidSymptoms = symptoms.stream()
                .filter(symptom -> !validSymptoms.contains(symptom.toLowerCase()))
                .collect(Collectors.toList());

        if (!invalidSymptoms.isEmpty()) {
            return "Triệu chứng không hợp lệ: " + String.join(", ", invalidSymptoms);
        }

        // 🟢 Lưu triệu chứng hợp lệ vào patient_symptoms
        symptoms.forEach(symptom -> patientSymptomRepo.save(PatientSymptom.builder()
                        .symptom(symptomsRepo.findByName(symptom))
                        .id(null)
                        .createdAt(LocalDateTime.now())
                        .sessionId(sessionId)
                .build()));

        // 🟢 Tìm danh sách bệnh có thể mắc
        List<DiseasesResponse> possibleDiseases = diseasesRepo.findDiseasesBySymptoms(symptoms, symptoms.size())
                .stream()
                .map(disease -> new DiseasesResponse(disease.getId(), disease.getName()))
                .collect(Collectors.toList());

        if (possibleDiseases.size() == 1) {
            // ✅ Nếu chỉ còn 1 bệnh, trả về kết quả
            patientSymptomRepo.deleteBySessionId(sessionId); // Xóa triệu chứng sau khi có kết quả
            return "Bạn có thể mắc bệnh: " + possibleDiseases.get(0).getName();
        }

        // 🔵 Nếu có nhiều bệnh, gọi Gemini để hỏi tiếp
        String nextQuestion = geminiService.getNextSymptomQuestion(sessionId, possibleDiseases);
        return nextQuestion;
    }
}
