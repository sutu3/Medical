package com.example.mediaservice.Controller;

import com.example.mediaservice.Dto.Request.MessageRequest;
import com.example.mediaservice.Dto.Response.DiagnosisSessionResponse;
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
    private final DiagnosisSessionRepo diagnosisSessionRepo;

    @PostMapping("/sessions")
    public DiagnosisSessionResponse startSession(@RequestParam String userIdentifier, @RequestBody MessageRequest content) {
        // Tạo mới phiên hội thoại và lưu tin nhắn triệu chứng

        return diagnosisService.createSessionWithMessage(userIdentifier, content.getContent());
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
    public String saveAnswer(@PathVariable Long id, @RequestBody MessageRequest answer) {
        DiagnosisSession session = sessionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Lưu câu trả lời vào câu hỏi hiện tại


        // Kiểm tra nếu đủ câu hỏi
        if (diagnosisService.isDiagnosisReady(session)) {
            return diagnosisService.ChuandoanBenh(session);
        } else {
            DiagnosisQuestion question = session.getQuestions().get(session.getQuestions().size() - 1);
            question.setAnswer(answer.getContent());
            sessionRepo.save(session);
            String questiongemini= diagnosisService.getNextQuestionAndSave(session);
            return questiongemini;
        }
    }
}
