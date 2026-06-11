package com.porraglobal.ranking.entity;

import com.porraglobal.leagues.entity.League;
import com.porraglobal.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "ranking_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"league_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id")
    private League league;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_points", nullable = false)
    @Builder.Default
    private int totalPoints = 0;

    @Column(name = "exact_hits", nullable = false)
    @Builder.Default
    private int exactHits = 0;

    @Column(name = "outcome_hits", nullable = false)
    @Builder.Default
    private int outcomeHits = 0;

    @Column(name = "current_position")
    private Integer currentPosition;

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
