package com.example.mediaservice.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiagnosisResponse {
    List<DiseasesResponse> possibleDiseases;
     String nextQuestion;
}
