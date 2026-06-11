package com.porraglobal.payments.dto;

import com.porraglobal.payments.entity.Subscription;

/**
 * Plan disponible. Los precios son orientativos para funciones premium
 * (no relacionadas con apuestas: la plataforma no maneja dinero real para predicciones).
 */
public record PlanResponse(
        Subscription.Plan plan,
        String displayName,
        String description,
        double monthlyPriceEur
) {
}
