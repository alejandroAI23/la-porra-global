package com.porraglobal.ranking.service;

import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.leagues.entity.LeagueMember;
import com.porraglobal.leagues.repository.LeagueMemberRepository;
import com.porraglobal.leagues.repository.LeagueRepository;
import com.porraglobal.predictions.entity.Prediction;
import com.porraglobal.predictions.repository.PredictionRepository;
import com.porraglobal.ranking.dto.RankingEntryResponse;
import com.porraglobal.ranking.entity.RankingEntry;
import com.porraglobal.ranking.repository.RankingEntryRepository;
import com.porraglobal.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final LeagueRepository leagueRepository;
    private final LeagueMemberRepository leagueMemberRepository;
    private final PredictionRepository predictionRepository;
    private final RankingEntryRepository rankingEntryRepository;
    private final ScoringService scoringService;

    /**
     * Recalcula y devuelve el ranking de una liga a partir de las predicciones
     * resueltas de sus miembros, ordenado por puntos y aciertos exactos.
     */
    @Transactional
    public List<RankingEntryResponse> getLeagueRanking(Long leagueId) {
        if (!leagueRepository.existsById(leagueId)) {
            throw new ResourceNotFoundException("Liga", leagueId);
        }

        List<LeagueMember> members = leagueMemberRepository.findByLeagueId(leagueId);
        List<RankingEntry> entries = new ArrayList<>();

        for (LeagueMember member : members) {
            User user = member.getUser();
            RankingEntry entry = rankingEntryRepository
                    .findByLeagueIdAndUserId(leagueId, user.getId())
                    .orElseGet(() -> RankingEntry.builder()
                            .league(member.getLeague()).user(user).build());

            recalculate(entry, user);
            entries.add(entry);
        }

        entries.sort(Comparator
                .comparingInt(RankingEntry::getTotalPoints).reversed()
                .thenComparing(Comparator.comparingInt(RankingEntry::getExactHits).reversed())
                .thenComparing(Comparator.comparingInt(RankingEntry::getOutcomeHits).reversed()));

        List<RankingEntryResponse> result = new ArrayList<>(entries.size());
        int position = 1;
        for (RankingEntry entry : entries) {
            entry.setCurrentPosition(position);
            entry.setUpdatedAt(Instant.now());
            rankingEntryRepository.save(entry);

            User user = entry.getUser();
            result.add(new RankingEntryResponse(position, user.getId(), user.getUsername(),
                    user.getDisplayName(), entry.getTotalPoints(), entry.getExactHits(),
                    entry.getOutcomeHits()));
            position++;
        }
        return result;
    }

    private void recalculate(RankingEntry entry, User user) {
        int totalPoints = 0;
        int exactHits = 0;
        int outcomeHits = 0;

        for (Prediction prediction : predictionRepository.findByUserId(user.getId())) {
            var match = prediction.getMatch();
            if (!scoringService.isResolvable(match)) {
                continue;
            }
            int points = scoringService.computePoints(prediction, match);
            totalPoints += points;
            if (scoringService.isExactHit(prediction, match)) {
                exactHits++;
            } else if (scoringService.isOutcomeHit(prediction, match)) {
                outcomeHits++;
            }
        }

        entry.setTotalPoints(totalPoints);
        entry.setExactHits(exactHits);
        entry.setOutcomeHits(outcomeHits);
    }
}
