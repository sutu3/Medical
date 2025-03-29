package com.example.mediaservice.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiseasesResponse {
    private Long id;
    private String name;
    /*private List<String> symptoms_name;*/
}
