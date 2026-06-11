package com.porraglobal.ai.controller;

import com.porraglobal.ai.dto.PredictionSuggestion;
import com.porraglobal.ai.service.PredictionAssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Asistente lúdico de predicciones (heurístico, sin garantías)")
public class AiController {

    private final PredictionAssistantService predictionAssistantService;

    @GetMapping("/matches/{matchId}/suggestion")
    @Operation(summary = "Sugerir un marcador para un partido",
            description = "Estimación heurística a partir de resultados previos. Ayuda lúdica, no garantiza aciertos.")
    public PredictionSuggestion suggest(@PathVariable Long matchId) {
        return predictionAssistantService.suggest(matchId);
    }
}
