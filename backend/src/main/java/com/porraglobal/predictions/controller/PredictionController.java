package com.porraglobal.predictions.controller;

import com.porraglobal.predictions.dto.CreatePredictionRequest;
import com.porraglobal.predictions.dto.PredictionResponse;
import com.porraglobal.predictions.service.PredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
@Tag(name = "Predictions", description = "Predicciones de resultados (cierran en el pitido inicial)")
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping
    @Operation(summary = "Enviar o actualizar una predicción",
            description = "Crea o actualiza la predicción del usuario para un partido antes del kickoff")
    public ResponseEntity<PredictionResponse> submit(@Valid @RequestBody CreatePredictionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(predictionService.submit(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Listar mis predicciones")
    public List<PredictionResponse> myPredictions() {
        return predictionService.getMyPredictions();
    }
}
