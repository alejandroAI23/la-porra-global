package com.porraglobal.ranking.controller;

import com.porraglobal.ranking.dto.RankingEntryResponse;
import com.porraglobal.ranking.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leagues/{leagueId}/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "Clasificación de las ligas")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    @Operation(summary = "Obtener el ranking de una liga",
            description = "Devuelve la clasificación ordenada por puntos y aciertos exactos")
    public List<RankingEntryResponse> getRanking(@PathVariable Long leagueId) {
        return rankingService.getLeagueRanking(leagueId);
    }
}
