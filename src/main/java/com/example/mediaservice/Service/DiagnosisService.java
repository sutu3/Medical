package com.example.mediaservice.Service;

import com.example.mediaservice.Entity.DiagnosisQuestion;
import com.example.mediaservice.Entity.DiagnosisSession;
import com.example.mediaservice.Entity.Message;
import com.example.mediaservice.Entity.QuestionAnswer;
import com.example.mediaservice.Repo.DiagnosisQuestionRepo;
import com.example.mediaservice.Repo.DiagnosisSessionRepo;
import com.example.mediaservice.Repo.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class DiagnosisService {
    MessageRepository messageRepo;
    DiagnosisSessionRepo sessionRepo;
    GeminiService geminiService;
    DiagnosisQuestionRepo questionRepo;

    // Xử lý lưu tin nhắn và triệu chứng
    // Tạo mới phiên hội thoại và lưu tin nhắn triệu chứng
    public DiagnosisSession createSessionWithMessage(String userIdentifier, String content) {
        // Tạo mới phiên hội thoại
        DiagnosisSession session = new DiagnosisSession();
        session.setUserIdentifier(userIdentifier);

        // Lưu tin nhắn triệu chứng
        Message message = new Message();
        message.setContent(content);
        messageRepo.save(message);

        // Lưu phiên hội thoại vào DB
        sessionRepo.save(session);

        return session;
    }

    // Lấy danh sách câu hỏi và câu trả lời trước đó
    public List<QuestionAnswer> getPreviousQuestionsAndAnswers(DiagnosisSession session) {
        return session.getQuestions().stream()
                .map(q -> new QuestionAnswer(q.getQuestionText(), q.getAnswer()))
                .collect(Collectors.toList());
    }

    // Gọi Gemini để lấy câu hỏi mới
    public String getNextQuestionAndSave(DiagnosisSession session) {
        List<QuestionAnswer> previousQuestionsAndAnswers = getPreviousQuestionsAndAnswers(session);

        // Gọi Gemini để đặt câu hỏi tiếp theo dựa trên các câu hỏi trước đó
        String questionFromGemini = geminiService.getNextSymptomQuestion(session.getId().toString(), previousQuestionsAndAnswers);

        // Lưu câu hỏi từ Gemini vào DB
        DiagnosisQuestion newQuestion = new DiagnosisQuestion();
        newQuestion.setQuestionText(questionFromGemini);
        newQuestion.setDiagnosisSession(session);
        questionRepo.save(newQuestion);

        return questionFromGemini;
    }

    // Kiểm tra nếu đủ câu hỏi (5 câu) để phán đoán
    public boolean isDiagnosisReady(DiagnosisSession session) {
        return session.getQuestions().size() >= 5;
    }
}
