package com.porraglobal.tournament.repository;

import com.porraglobal.tournament.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByCode(String code);
}
