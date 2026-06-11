package com.porraglobal.tournament.service;

import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.tournament.dto.MatchResponse;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.entity.Team;
import com.porraglobal.tournament.mapper.TournamentMapper;
import com.porraglobal.tournament.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    private MatchService matchService;

    private Match match;

    @BeforeEach
    void setUp() {
        var mapper = Mappers.getMapper(TournamentMapper.class);
        matchService = new MatchService(matchRepository, mapper);

        var home = Team.builder().id(1L).name("España").code("ESP").build();
        var away = Team.builder().id(2L).name("Argentina").code("ARG").build();
        match = Match.builder()
                .id(10L)
                .homeTeam(home)
                .awayTeam(away)
                .kickoffAt(Instant.parse("2026-06-12T18:00:00Z"))
                .stage(Match.Stage.GROUP)
                .status(Match.Status.SCHEDULED)
                .build();
    }

    @Test
    void getMatches_shouldReturnAll_whenStatusIsNull() {
        when(matchRepository.findAllByOrderByKickoffAtAsc()).thenReturn(List.of(match));

        List<MatchResponse> result = matchService.getMatches(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).homeTeam().code()).isEqualTo("ESP");
        assertThat(result.get(0).awayTeam().code()).isEqualTo("ARG");
    }

    @Test
    void getMatches_shouldFilterByStatus() {
        when(matchRepository.findByStatusOrderByKickoffAtAsc(Match.Status.FINISHED))
                .thenReturn(List.of());

        List<MatchResponse> result = matchService.getMatches(Match.Status.FINISHED);

        assertThat(result).isEmpty();
    }

    @Test
    void getMatch_shouldThrow_whenNotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.getMatch(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
