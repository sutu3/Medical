package com.example.mediaservice.Repo;

import com.example.mediaservice.Entity.DiagnosisQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisQuestionRepo extends JpaRepository<DiagnosisQuestion, Long> {}
