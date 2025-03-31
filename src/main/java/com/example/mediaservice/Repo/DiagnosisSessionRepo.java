package com.example.mediaservice.Repo;

import com.example.mediaservice.Entity.DiagnosisSession;
import com.example.mediaservice.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisSessionRepo extends JpaRepository<DiagnosisSession, Long> {}
