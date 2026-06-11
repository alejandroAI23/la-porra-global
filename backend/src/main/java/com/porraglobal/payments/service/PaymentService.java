package com.porraglobal.payments.service;

import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.payments.dto.PlanResponse;
import com.porraglobal.payments.dto.SubscribeRequest;
import com.porraglobal.payments.dto.SubscriptionResponse;
import com.porraglobal.payments.entity.Subscription;
import com.porraglobal.payments.mapper.SubscriptionMapper;
import com.porraglobal.payments.repository.SubscriptionRepository;
import com.porraglobal.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Gestión de suscripciones a planes premium (funciones extra).
 * NOTA: no procesa apuestas ni dinero real para predicciones. La integración con un
 * proveedor de pagos (p. ej. Stripe) se simula aquí mediante una referencia externa.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final CurrentUserProvider currentUserProvider;

    public List<PlanResponse> getPlans() {
        return List.of(
                new PlanResponse(Subscription.Plan.FREE, "Gratis",
                        "Ligas de hasta 50 miembros y predicciones básicas", 0.0),
                new PlanResponse(Subscription.Plan.PREMIUM, "Premium",
                        "Ligas grandes, estadísticas avanzadas y asistente de IA", 2.99),
                new PlanResponse(Subscription.Plan.BAR_PRO, "Bar Pro",
                        "Gestión de eventos y promoción de tu bar", 9.99)
        );
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getActiveSubscription() {
        Long userId = currentUserProvider.requireId();
        return subscriptionRepository
                .findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
                .map(subscriptionMapper::toResponse)
                .orElseGet(() -> new SubscriptionResponse(null, Subscription.Plan.FREE,
                        Subscription.Status.ACTIVE, null, null));
    }

    /**
     * Activa un plan para el usuario. Cancela la suscripción activa previa.
     * No cobra dinero real: registra la intención y delega el cobro al proveedor externo.
     */
    @Transactional
    public SubscriptionResponse subscribe(SubscribeRequest request) {
        User user = currentUserProvider.require();

        subscriptionRepository.findByUserIdAndStatus(user.getId(), Subscription.Status.ACTIVE)
                .ifPresent(active -> {
                    active.setStatus(Subscription.Status.CANCELLED);
                    active.setEndsAt(Instant.now());
                    subscriptionRepository.save(active);
                });

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(request.plan())
                .status(Subscription.Status.ACTIVE)
                .startsAt(Instant.now())
                .endsAt(request.plan() == Subscription.Plan.FREE
                        ? null
                        : Instant.now().plus(30, ChronoUnit.DAYS))
                .build();
        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponse cancel() {
        Long userId = currentUserProvider.requireId();
        return subscriptionRepository
                .findByUserIdAndStatus(userId, Subscription.Status.ACTIVE)
                .map(active -> {
                    active.setStatus(Subscription.Status.CANCELLED);
                    active.setEndsAt(Instant.now());
                    return subscriptionMapper.toResponse(subscriptionRepository.save(active));
                })
                .orElseGet(() -> new SubscriptionResponse(null, Subscription.Plan.FREE,
                        Subscription.Status.ACTIVE, null, null));
    }
}
