package com.porraglobal.ranking.service;

import com.porraglobal.predictions.entity.Prediction;
import com.porraglobal.tournament.entity.Match;
import org.springframework.stereotype.Component;

/**
 * Reglas de puntuación de las predicciones.
 * - Resultado exacto: 3 puntos.
 * - Acierto del signo (1, X, 2) sin resultado exacto: 1 punto.
 * - Fallo: 0 puntos.
 */
@Component
public class ScoringService {

    public static final int EXACT_POINTS = 3;
    public static final int OUTCOME_POINTS = 1;

    public boolean isResolvable(Match match) {
        return match.getStatus() == Match.Status.FINISHED
                && match.getHomeScore() != null
                && match.getAwayScore() != null;
    }

    public int computePoints(Prediction prediction, Match match) {
        if (!isResolvable(match)) {
            return 0;
        }
        if (isExactHit(prediction, match)) {
            return EXACT_POINTS;
        }
        if (isOutcomeHit(prediction, match)) {
            return OUTCOME_POINTS;
        }
        return 0;
    }

    public boolean isExactHit(Prediction p, Match m) {
        return p.getPredictedHomeScore() == m.getHomeScore()
                && p.getPredictedAwayScore() == m.getAwayScore();
    }

    public boolean isOutcomeHit(Prediction p, Match m) {
        return Integer.signum(p.getPredictedHomeScore() - p.getPredictedAwayScore())
                == Integer.signum(m.getHomeScore() - m.getAwayScore());
    }
}
