package com.example.mediaservice.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@DynamicInsert
@DynamicUpdate
public class DiagnosisSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userIdentifier;

    @OneToMany(mappedBy = "diagnosisSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiagnosisQuestion> questions = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}