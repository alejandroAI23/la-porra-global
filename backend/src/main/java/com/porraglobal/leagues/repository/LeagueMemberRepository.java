package com.porraglobal.leagues.repository;

import com.porraglobal.leagues.entity.LeagueMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeagueMemberRepository extends JpaRepository<LeagueMember, Long> {

    List<LeagueMember> findByLeagueId(Long leagueId);

    List<LeagueMember> findByUserId(Long userId);

    Optional<LeagueMember> findByLeagueIdAndUserId(Long leagueId, Long userId);

    boolean existsByLeagueIdAndUserId(Long leagueId, Long userId);

    long countByLeagueId(Long leagueId);
}
