package com.porraglobal.tournament.repository;

import com.porraglobal.tournament.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatusOrderByKickoffAtAsc(Match.Status status);

    List<Match> findByKickoffAtBetweenOrderByKickoffAtAsc(Instant from, Instant to);

    List<Match> findAllByOrderByKickoffAtAsc();
}
