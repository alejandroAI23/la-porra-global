package com.porraglobal.ranking.service;

import com.porraglobal.predictions.entity.Prediction;
import com.porraglobal.tournament.entity.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoringServiceTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringService();
    }

    private Match finished(int home, int away) {
        return Match.builder().status(Match.Status.FINISHED).homeScore(home).awayScore(away).build();
    }

    private Prediction prediction(int home, int away) {
        return Prediction.builder().predictedHomeScore(home).predictedAwayScore(away).build();
    }

    @Test
    void exactResult_shouldGiveThreePoints() {
        int points = scoringService.computePoints(prediction(2, 1), finished(2, 1));
        assertThat(points).isEqualTo(ScoringService.EXACT_POINTS);
    }

    @Test
    void correctOutcomeWrongScore_shouldGiveOnePoint() {
        int points = scoringService.computePoints(prediction(3, 1), finished(2, 1));
        assertThat(points).isEqualTo(ScoringService.OUTCOME_POINTS);
    }

    @Test
    void correctDraw_shouldGiveThreePoints_whenExact() {
        assertThat(scoringService.computePoints(prediction(1, 1), finished(1, 1)))
                .isEqualTo(ScoringService.EXACT_POINTS);
    }

    @Test
    void drawOutcome_shouldGiveOnePoint_whenScoreDiffers() {
        assertThat(scoringService.computePoints(prediction(0, 0), finished(2, 2)))
                .isEqualTo(ScoringService.OUTCOME_POINTS);
    }

    @Test
    void wrongOutcome_shouldGiveZero() {
        assertThat(scoringService.computePoints(prediction(0, 2), finished(2, 0))).isZero();
    }

    @Test
    void unfinishedMatch_shouldGiveZeroAndNotBeResolvable() {
        var scheduled = Match.builder().status(Match.Status.SCHEDULED).build();
        assertThat(scoringService.isResolvable(scheduled)).isFalse();
        assertThat(scoringService.computePoints(prediction(1, 0), scheduled)).isZero();
    }
}
