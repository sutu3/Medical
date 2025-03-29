package com.example.mediaservice.Service;


import com.example.mediaservice.Entity.PatientSymptom;
import com.example.mediaservice.Entity.Symptoms;
import com.example.mediaservice.Repo.PatientSymptomRepo;
import com.example.mediaservice.Repo.SymptomsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientSymptomService {
    private final PatientSymptomRepo patientSymptomRepo;
    private final SymptomsRepo symptomsRepo;

    // 🟢 Thêm triệu chứng cho một session
    public void addSymptom(String sessionId, String symptomId) {
        Symptoms symptom = symptomsRepo.findById(symptomId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy triệu chứng"));
        PatientSymptom patientSymptom = new PatientSymptom();
        patientSymptom.setSessionId(sessionId);
        patientSymptom.setSymptom(symptom);
        patientSymptomRepo.save(patientSymptom);
    }

    // 🔵 Lấy danh sách triệu chứng của session hiện tại
    public List<PatientSymptom> getSymptomsBySession(String sessionId) {
        return patientSymptomRepo.findBySessionId(sessionId);
    }

    // 🛑 Xóa triệu chứng sau khi chẩn đoán xong
    public void clearSymptoms(String sessionId) {
        patientSymptomRepo.deleteBySessionId(sessionId);
    }
}
