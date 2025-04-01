package com.example.mediaservice.Dto.Response;

import com.example.mediaservice.Entity.DiagnosisQuestion;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiagnosisSessionResponse {
    private Long id;

    private String userIdentifier;

    private List<DiagnosisQuestionResponse> questions ;

    private LocalDateTime createdAt;
}