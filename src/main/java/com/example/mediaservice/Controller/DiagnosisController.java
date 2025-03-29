package com.example.mediaservice.Controller;

import com.example.mediaservice.Dto.Response.DiagnosisResponse;
import com.example.mediaservice.Entity.Diseases;
import com.example.mediaservice.Service.DiseasesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnosis")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class DiagnosisController {
    DiseasesService diseasesService;

    @PostMapping("/diagnose")
    public String diagnose(@RequestParam String sessionId, @RequestBody List<String> symptoms) {
        return diseasesService.diagnose(sessionId, symptoms);
    }
}
