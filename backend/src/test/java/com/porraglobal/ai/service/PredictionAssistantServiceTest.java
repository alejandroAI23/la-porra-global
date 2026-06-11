package com.porraglobal.ai.service;

import com.porraglobal.ai.dto.PredictionSuggestion;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.entity.Team;
import com.porraglobal.tournament.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PredictionAssistantServiceTest {

    @Mock
    private MatchRepository matchRepository;

    private PredictionAssistantService service;

    private Team home;
    private Team away;

    @BeforeEach
    void setUp() {
        service = new PredictionAssistantService(matchRepository);
        home = Team.builder().id(1L).name("España").code("ESP").build();
        away = Team.builder().id(2L).name("Argentina").code("ARG").build();
    }

    @Test
    void suggest_shouldThrow_whenMatchNotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.suggest(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void suggest_shouldReturnLowConfidence_whenNoHistory() {
        var match = Match.builder().id(10L).homeTeam(home).awayTeam(away)
                .kickoffAt(Instant.now()).status(Match.Status.SCHEDULED).build();
        when(matchRepository.findById(10L)).thenReturn(Optional.of(match));
        when(matchRepository.findByStatusOrderByKickoffAtAsc(Match.Status.FINISHED))
                .thenReturn(List.of());

        PredictionSuggestion suggestion = service.suggest(10L);

        assertThat(suggestion.matchId()).isEqualTo(10L);
        assertThat(suggestion.confidence()).isEqualTo("BAJA");
        assertThat(suggestion.suggestedHomeScore()).isGreaterThanOrEqualTo(0);
        assertThat(suggestion.suggestedAwayScore()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void suggest_shouldUseHistory_whenFinishedMatchesExist() {
        var match = Match.builder().id(10L).homeTeam(home).awayTeam(away)
                .kickoffAt(Instant.now()).status(Match.Status.SCHEDULED).build();
        // España marca mucho, Argentina encaja: sugerencia favorable al local.
        var h1 = Match.builder().id(1L).homeTeam(home).awayTeam(Team.builder().id(3L).build())
                .status(Match.Status.FINISHED).homeScore(3).awayScore(0).build();
        var h2 = Match.builder().id(2L).homeTeam(home).awayTeam(Team.builder().id(4L).build())
                .status(Match.Status.FINISHED).homeScore(4).awayScore(1).build();
        var a1 = Match.builder().id(3L).homeTeam(Team.builder().id(5L).build()).awayTeam(away)
                .status(Match.Status.FINISHED).homeScore(3).awayScore(0).build();
        var a2 = Match.builder().id(4L).homeTeam(Team.builder().id(6L).build()).awayTeam(away)
                .status(Match.Status.FINISHED).homeScore(2).awayScore(0).build();

        when(matchRepository.findById(10L)).thenReturn(Optional.of(match));
        when(matchRepository.findByStatusOrderByKickoffAtAsc(Match.Status.FINISHED))
                .thenReturn(List.of(h1, h2, a1, a2));

        PredictionSuggestion suggestion = service.suggest(10L);

        assertThat(suggestion.confidence()).isEqualTo("ALTA");
        assertThat(suggestion.suggestedHomeScore())
                .isGreaterThan(suggestion.suggestedAwayScore());
    }
}
