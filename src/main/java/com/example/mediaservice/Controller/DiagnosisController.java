package com.example.mediaservice.Controller;

import com.example.mediaservice.Entity.DiagnosisQuestion;
import com.example.mediaservice.Entity.DiagnosisSession;
import com.example.mediaservice.Repo.DiagnosisSessionRepo;
import com.example.mediaservice.Service.DiagnosisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class DiagnosisController {
    DiagnosisService diagnosisService;
    DiagnosisSessionRepo sessionRepo;

    @PostMapping("/sessions")
    public DiagnosisSession startSession(@RequestParam String userIdentifier, @RequestBody String content) {
        // Tạo mới phiên hội thoại và lưu tin nhắn triệu chứng
        return diagnosisService.createSessionWithMessage(userIdentifier, content);
    }

    @GetMapping("/sessions/{id}/questions")
    public String askNextQuestion(@PathVariable Long id) {
        // Lấy thông tin session
        DiagnosisSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Gọi Gemini để lấy câu hỏi tiếp theo
        String question = diagnosisService.getNextQuestionAndSave(session);

        return question;  // Trả lại câu hỏi cho người dùng
    }

    @PostMapping("/sessions/{id}/answers")
    public String saveAnswer(@PathVariable Long id, @RequestBody String answer) {
        DiagnosisSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Lưu câu trả lời vào câu hỏi hiện tại
        DiagnosisQuestion question = session.getQuestions().get(session.getQuestions().size() - 1);
        question.setAnswer(answer);
        sessionRepo.save(session);

        // Kiểm tra nếu đủ câu hỏi
        if (diagnosisService.isDiagnosisReady(session)) {
            return "Chẩn đoán đã sẵn sàng!";
        } else {
            return "Câu hỏi tiếp theo đã được gửi.";
        }
    }
}
