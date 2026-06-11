package com.porraglobal.predictions.service;

import com.porraglobal.common.exception.BadRequestException;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.predictions.dto.CreatePredictionRequest;
import com.porraglobal.predictions.entity.Prediction;
import com.porraglobal.predictions.mapper.PredictionMapper;
import com.porraglobal.predictions.repository.PredictionRepository;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.repository.MatchRepository;
import com.porraglobal.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock
    private PredictionRepository predictionRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    private PredictionService predictionService;

    private User user;

    @BeforeEach
    void setUp() {
        var mapper = Mappers.getMapper(PredictionMapper.class);
        predictionService = new PredictionService(predictionRepository, matchRepository,
                mapper, currentUserProvider);
        user = User.builder().id(1L).username("juan").build();
    }

    private Match futureMatch() {
        return Match.builder()
                .id(10L)
                .kickoffAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .status(Match.Status.SCHEDULED)
                .build();
    }

    @Test
    void submit_shouldCreateNewPrediction() {
        when(currentUserProvider.require()).thenReturn(user);
        when(matchRepository.findById(10L)).thenReturn(Optional.of(futureMatch()));
        when(predictionRepository.findByUserIdAndMatchId(1L, 10L)).thenReturn(Optional.empty());
        when(predictionRepository.save(any(Prediction.class))).thenAnswer(inv -> {
            Prediction p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        var response = predictionService.submit(new CreatePredictionRequest(10L, 2, 1));

        assertThat(response.predictedHomeScore()).isEqualTo(2);
        assertThat(response.predictedAwayScore()).isEqualTo(1);
        assertThat(response.matchId()).isEqualTo(10L);
    }

    @Test
    void submit_shouldUpdateExistingPrediction() {
        var existing = Prediction.builder().id(100L).user(user).match(futureMatch())
                .predictedHomeScore(0).predictedAwayScore(0).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(matchRepository.findById(10L)).thenReturn(Optional.of(futureMatch()));
        when(predictionRepository.findByUserIdAndMatchId(1L, 10L)).thenReturn(Optional.of(existing));
        when(predictionRepository.save(any(Prediction.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = predictionService.submit(new CreatePredictionRequest(10L, 3, 2));

        assertThat(response.predictedHomeScore()).isEqualTo(3);
        assertThat(response.updatedAt()).isNotNull();
    }

    @Test
    void submit_shouldThrow_whenMatchAlreadyStarted() {
        var started = Match.builder().id(10L)
                .kickoffAt(Instant.now().minus(1, ChronoUnit.HOURS))
                .status(Match.Status.SCHEDULED).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(matchRepository.findById(10L)).thenReturn(Optional.of(started));

        assertThatThrownBy(() -> predictionService.submit(new CreatePredictionRequest(10L, 1, 0)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("cerrado");
        verify(predictionRepository, never()).save(any());
    }

    @Test
    void submit_shouldThrow_whenMatchNotScheduled() {
        var live = Match.builder().id(10L)
                .kickoffAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .status(Match.Status.LIVE).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(matchRepository.findById(10L)).thenReturn(Optional.of(live));

        assertThatThrownBy(() -> predictionService.submit(new CreatePredictionRequest(10L, 1, 0)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("programados");
    }

    @Test
    void submit_shouldThrow_whenMatchNotFound() {
        when(currentUserProvider.require()).thenReturn(user);
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> predictionService.submit(new CreatePredictionRequest(99L, 1, 0)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
