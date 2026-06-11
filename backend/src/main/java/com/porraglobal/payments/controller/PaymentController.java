package com.porraglobal.payments.controller;

import com.porraglobal.payments.dto.PlanResponse;
import com.porraglobal.payments.dto.SubscribeRequest;
import com.porraglobal.payments.dto.SubscriptionResponse;
import com.porraglobal.payments.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Planes premium y suscripciones (sin apuestas con dinero real)")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/plans")
    @Operation(summary = "Listar planes disponibles")
    public List<PlanResponse> plans() {
        return paymentService.getPlans();
    }

    @GetMapping("/subscription")
    @Operation(summary = "Obtener mi suscripción activa")
    public SubscriptionResponse subscription() {
        return paymentService.getActiveSubscription();
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Suscribirse a un plan")
    public SubscriptionResponse subscribe(@Valid @RequestBody SubscribeRequest request) {
        return paymentService.subscribe(request);
    }

    @PostMapping("/cancel")
    @Operation(summary = "Cancelar mi suscripción activa")
    public SubscriptionResponse cancel() {
        return paymentService.cancel();
    }
}
