package com.example.mediaservice.Service;

import com.example.mediaservice.Dto.Response.DiagnosisSessionResponse;
import com.example.mediaservice.Entity.DiagnosisQuestion;
import com.example.mediaservice.Entity.DiagnosisSession;
import com.example.mediaservice.Entity.Message;
import com.example.mediaservice.Entity.QuestionAnswer;
import com.example.mediaservice.Mapper.DiagnosisSessionResponseMapper;
import com.example.mediaservice.Repo.DiagnosisQuestionRepo;
import com.example.mediaservice.Repo.DiagnosisSessionRepo;
import com.example.mediaservice.Repo.MessageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    DiagnosisSessionResponseMapper diagnosisSessionResponseMapper;
    private final DiagnosisQuestionRepo diagnosisQuestionRepo;
    private final DiagnosisSessionRepo diagnosisSessionRepo;

    // Xử lý lưu tin nhắn và triệu chứng
    // Tạo mới phiên hội thoại và lưu tin nhắn triệu chứng
    public DiagnosisSessionResponse createSessionWithMessage(String userIdentifier, String content) {
        // Tạo mới phiên hội thoại
        DiagnosisSession session = new DiagnosisSession();
        session.setUserIdentifier(userIdentifier);

        // Lưu tin nhắn triệu chứng
        Message message = new Message();
        message.setContent(content);
        messageRepo.save(message);

        // Lưu phiên hội thoại vào DB
        sessionRepo.save(session);

        // Gọi Gemini để tạo câu hỏi đầu tiên
        String firstQuestion = geminiService.getNextSymptomQuestion(session.getId().toString(), List.of(),false,content);
        System.out.println(firstQuestion);
        // Lưu câu hỏi đầu tiên vào DB
        DiagnosisQuestion newQuestion = new DiagnosisQuestion();
        newQuestion.setQuestionText(firstQuestion);
        newQuestion.setDiagnosisSession(session);
        questionRepo.save(newQuestion);

        // Thêm câu hỏi vào danh sách questions
        session.getQuestions().add(newQuestion);
        sessionRepo.save(session);  // Lưu lại phiên hội thoại đã cập nhật câu hỏi

        return diagnosisSessionResponseMapper.toDiagnosisSessionResponse(session);  // Trả về session đã tạo
    }


    // Lấy danh sách câu hỏi và câu trả lời trước đó
    public List<QuestionAnswer> getPreviousQuestionsAndAnswers(DiagnosisSession session) {
        if (session.getQuestions() == null) {
            return new ArrayList<>();  // Nếu questions là null, trả về một danh sách rỗng
        }

        return session.getQuestions().stream()
                .map(q -> new QuestionAnswer(q.getQuestionText(), q.getAnswer()))
                .collect(Collectors.toList());
    }
    public String ChuandoanBenh(DiagnosisSession session) {
        List<QuestionAnswer> previousQuestionsAndAnswers = getPreviousQuestionsAndAnswers(session);

        // Gọi Gemini để đặt câu hỏi tiếp theo dựa trên các câu hỏi trước đó
        String questionFromGemini = geminiService.getMyAnwer(session.getId().toString(), previousQuestionsAndAnswers);
        System.out.println(questionFromGemini);
        // Lưu câu hỏi từ Gemini vào DB
        diagnosisSessionRepo.deleteAll();
        diagnosisQuestionRepo.deleteAll();
        messageRepo.deleteAll();

        return questionFromGemini;
    }
    // Gọi Gemini để lấy câu hỏi mới
    public String getNextQuestionAndSave(DiagnosisSession session) {
        List<QuestionAnswer> previousQuestionsAndAnswers = getPreviousQuestionsAndAnswers(session);

        // Gọi Gemini để đặt câu hỏi tiếp theo dựa trên các câu hỏi trước đó
        String questionFromGemini = geminiService.getNextSymptomQuestion(session.getId().toString(), previousQuestionsAndAnswers,true,"");
        System.out.println(questionFromGemini);
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
