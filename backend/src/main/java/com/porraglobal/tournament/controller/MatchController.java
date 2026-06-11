package com.porraglobal.tournament.controller;

import com.porraglobal.tournament.dto.MatchResponse;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Tournament", description = "Partidos y selecciones del torneo")
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    @Operation(summary = "Listar partidos", description = "Devuelve los partidos ordenados por fecha; filtrable por estado")
    public List<MatchResponse> getMatches(@RequestParam(required = false) Match.Status status) {
        return matchService.getMatches(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un partido por id")
    public MatchResponse getMatch(@PathVariable Long id) {
        return matchService.getMatch(id);
    }
}
