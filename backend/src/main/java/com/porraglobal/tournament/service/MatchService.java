package com.porraglobal.tournament.service;

import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.tournament.dto.MatchResponse;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.mapper.TournamentMapper;
import com.porraglobal.tournament.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final TournamentMapper tournamentMapper;

    @Transactional(readOnly = true)
    public List<MatchResponse> getMatches(Match.Status status) {
        List<Match> matches = (status == null)
                ? matchRepository.findAllByOrderByKickoffAtAsc()
                : matchRepository.findByStatusOrderByKickoffAtAsc(status);
        return matches.stream().map(tournamentMapper::toMatchResponse).toList();
    }

    @Transactional(readOnly = true)
    public MatchResponse getMatch(Long id) {
        return matchRepository.findById(id)
                .map(tournamentMapper::toMatchResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Partido", id));
    }
}
