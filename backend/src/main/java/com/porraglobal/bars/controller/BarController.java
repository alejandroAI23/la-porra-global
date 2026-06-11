package com.porraglobal.bars.controller;

import com.porraglobal.bars.dto.BarEventResponse;
import com.porraglobal.bars.dto.BarResponse;
import com.porraglobal.bars.dto.CreateBarEventRequest;
import com.porraglobal.bars.dto.CreateBarRequest;
import com.porraglobal.bars.service.BarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bars")
@RequiredArgsConstructor
@Tag(name = "Bars", description = "Bares y eventos para ver los partidos")
public class BarController {

    private final BarService barService;

    @GetMapping
    @Operation(summary = "Listar bares", description = "Filtrable por ciudad")
    public List<BarResponse> getBars(@RequestParam(required = false) String city) {
        return barService.getBars(city);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un bar por id")
    public BarResponse getBar(@PathVariable Long id) {
        return barService.getBar(id);
    }

    @PostMapping
    @Operation(summary = "Registrar un bar", description = "El usuario autenticado queda como propietario")
    public ResponseEntity<BarResponse> createBar(@Valid @RequestBody CreateBarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barService.createBar(request));
    }

    @GetMapping("/{id}/events")
    @Operation(summary = "Listar eventos de un bar")
    public List<BarEventResponse> getBarEvents(@PathVariable Long id) {
        return barService.getBarEvents(id);
    }

    @PostMapping("/{id}/events")
    @Operation(summary = "Crear un evento en un bar")
    public ResponseEntity<BarEventResponse> createBarEvent(@PathVariable Long id,
                                                          @Valid @RequestBody CreateBarEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(barService.createBarEvent(id, request));
    }
}
