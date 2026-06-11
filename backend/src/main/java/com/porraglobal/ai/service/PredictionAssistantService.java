package com.porraglobal.ai.service;

import com.porraglobal.ai.dto.PredictionSuggestion;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.entity.Team;
import com.porraglobal.tournament.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Asistente de predicciones basado en heurísticas sobre los resultados ya jugados.
 * No usa servicios externos ni datos oficiales: estima un marcador a partir de la
 * media de goles marcados y encajados por cada selección en partidos FINISHED.
 * Es una ayuda lúdica, sin garantías sobre los resultados reales.
 */
@Service
@RequiredArgsConstructor
public class PredictionAssistantService {

    private static final double DEFAULT_ATTACK = 1.2;
    private static final double DEFAULT_DEFENSE = 1.2;

    private final MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public PredictionSuggestion suggest(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Partido", matchId));

        List<Match> finished = matchRepository.findByStatusOrderByKickoffAtAsc(Match.Status.FINISHED);

        double homeAttack = averageGoalsScored(match.getHomeTeam(), finished, DEFAULT_ATTACK);
        double homeDefense = averageGoalsConceded(match.getHomeTeam(), finished, DEFAULT_DEFENSE);
        double awayAttack = averageGoalsScored(match.getAwayTeam(), finished, DEFAULT_ATTACK);
        double awayDefense = averageGoalsConceded(match.getAwayTeam(), finished, DEFAULT_DEFENSE);

        // Ventaja de campo leve para el equipo local.
        double expectedHome = (homeAttack + awayDefense) / 2.0 + 0.2;
        double expectedAway = (awayAttack + homeDefense) / 2.0;

        int suggestedHome = (int) Math.round(expectedHome);
        int suggestedAway = (int) Math.round(expectedAway);

        long samples = finished.stream()
                .filter(m -> involves(m, match.getHomeTeam()) || involves(m, match.getAwayTeam()))
                .count();
        String confidence = samples >= 4 ? "ALTA" : samples >= 2 ? "MEDIA" : "BAJA";

        String rationale = "Estimación basada en %d partidos previos de ambas selecciones. Goles esperados: %.1f-%.1f."
                .formatted(samples, expectedHome, expectedAway);

        return new PredictionSuggestion(match.getId(), match.getHomeTeam().getName(),
                match.getAwayTeam().getName(), suggestedHome, suggestedAway, confidence, rationale);
    }

    private double averageGoalsScored(Team team, List<Match> finished, double fallback) {
        int games = 0;
        int goals = 0;
        for (Match m : finished) {
            if (m.getHomeTeam().getId().equals(team.getId())) {
                goals += m.getHomeScore();
                games++;
            } else if (m.getAwayTeam().getId().equals(team.getId())) {
                goals += m.getAwayScore();
                games++;
            }
        }
        return games == 0 ? fallback : (double) goals / games;
    }

    private double averageGoalsConceded(Team team, List<Match> finished, double fallback) {
        int games = 0;
        int goals = 0;
        for (Match m : finished) {
            if (m.getHomeTeam().getId().equals(team.getId())) {
                goals += m.getAwayScore();
                games++;
            } else if (m.getAwayTeam().getId().equals(team.getId())) {
                goals += m.getHomeScore();
                games++;
            }
        }
        return games == 0 ? fallback : (double) goals / games;
    }

    private boolean involves(Match m, Team team) {
        return m.getHomeTeam().getId().equals(team.getId())
                || m.getAwayTeam().getId().equals(team.getId());
    }
}
