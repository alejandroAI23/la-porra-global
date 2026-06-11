package com.porraglobal.predictions.entity;

import com.porraglobal.tournament.entity.Match;
import com.porraglobal.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "predictions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "match_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "predicted_home_score", nullable = false)
    private int predictedHomeScore;

    @Column(name = "predicted_away_score", nullable = false)
    private int predictedAwayScore;

    /** Puntos obtenidos tras resolverse el partido. Null hasta entonces. */
    @Column(name = "points_awarded")
    private Integer pointsAwarded;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt;
}
