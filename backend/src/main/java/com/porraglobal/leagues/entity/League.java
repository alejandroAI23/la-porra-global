package com.porraglobal.leagues.entity;

import com.porraglobal.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "leagues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class League {

    public enum Type {
        FRIENDS, COMPANY, BAR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 255)
    private String description;

    /** Código de invitación único para unirse a la liga. */
    @Column(name = "invite_code", nullable = false, unique = true, length = 10)
    private String inviteCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Type type = Type.FRIENDS;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "max_members", nullable = false)
    @Builder.Default
    private int maxMembers = 50;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
