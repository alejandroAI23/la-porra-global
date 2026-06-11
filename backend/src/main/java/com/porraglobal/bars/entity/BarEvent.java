package com.porraglobal.bars.entity;

import com.porraglobal.tournament.entity.Match;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "bar_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bar_id")
    private Bar bar;

    /** Partido asociado al evento (opcional: puede ser un evento genérico). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "starts_at", nullable = false)
    private Instant startsAt;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
