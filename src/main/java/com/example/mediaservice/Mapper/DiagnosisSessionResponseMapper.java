package com.example.mediaservice.Mapper;

import com.example.mediaservice.Dto.Response.DiagnosisSessionResponse;
import com.example.mediaservice.Entity.DiagnosisSession;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiagnosisSessionResponseMapper {
    DiagnosisSessionResponse toDiagnosisSessionResponse(DiagnosisSession entity);
}
