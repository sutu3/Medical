package com.example.mediaservice.Repo;

import com.example.mediaservice.Entity.PatientSymptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientSymptomRepo extends JpaRepository<PatientSymptom, Long> {
    List<PatientSymptom> findBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
}