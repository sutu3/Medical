package com.example.mediaservice.Dto.Response;

import com.example.mediaservice.Entity.DiagnosisSession;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiagnosisQuestionResponse {
    private Long id;

    private String questionText;

    private String answer;

    private LocalDateTime askedAt ;

}