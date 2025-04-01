package com.example.mediaservice.Dto.Response;

import com.example.mediaservice.Entity.DiagnosisSession;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    private String id;

    private String content; // Tin nhắn người dùng

    private DiagnosisSession session; // Gắn với một phiên chẩn đoán
}