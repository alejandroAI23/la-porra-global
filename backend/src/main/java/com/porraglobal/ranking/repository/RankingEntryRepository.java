package com.porraglobal.ranking.repository;

import com.porraglobal.ranking.entity.RankingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingEntryRepository extends JpaRepository<RankingEntry, Long> {

    List<RankingEntry> findByLeagueIdOrderByTotalPointsDescExactHitsDesc(Long leagueId);

    Optional<RankingEntry> findByLeagueIdAndUserId(Long leagueId, Long userId);
}
