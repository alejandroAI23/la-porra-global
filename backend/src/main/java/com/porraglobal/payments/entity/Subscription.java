package com.porraglobal.payments.entity;

import com.porraglobal.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Suscripción a planes premium (funciones extra: ligas más grandes, estadísticas, IA).
 * NO está relacionada con apuestas: la plataforma no maneja dinero real para predicciones.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    public enum Plan {
        FREE, PREMIUM, BAR_PRO
    }

    public enum Status {
        ACTIVE, CANCELLED, EXPIRED, PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Plan plan = Plan.FREE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Status status = Status.ACTIVE;

    /** Referencia externa del proveedor de pagos (p. ej. Stripe). */
    @Column(name = "external_ref", length = 100)
    private String externalRef;

    @Column(name = "starts_at", nullable = false)
    @Builder.Default
    private Instant startsAt = Instant.now();

    @Column(name = "ends_at")
    private Instant endsAt;
}
