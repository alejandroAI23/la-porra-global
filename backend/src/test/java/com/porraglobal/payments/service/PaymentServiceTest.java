package com.porraglobal.payments.service;

import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.payments.dto.SubscribeRequest;
import com.porraglobal.payments.entity.Subscription;
import com.porraglobal.payments.mapper.SubscriptionMapper;
import com.porraglobal.payments.repository.SubscriptionRepository;
import com.porraglobal.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    private PaymentService paymentService;

    private User user;

    @BeforeEach
    void setUp() {
        var mapper = Mappers.getMapper(SubscriptionMapper.class);
        paymentService = new PaymentService(subscriptionRepository, mapper, currentUserProvider);
        user = User.builder().id(1L).username("juan").build();
    }

    @Test
    void getPlans_shouldListThreePlans() {
        assertThat(paymentService.getPlans()).hasSize(3);
    }

    @Test
    void subscribe_shouldCancelPreviousActiveAndCreateNew() {
        var previous = Subscription.builder().id(1L).user(user)
                .plan(Subscription.Plan.FREE).status(Subscription.Status.ACTIVE).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(subscriptionRepository.findByUserIdAndStatus(1L, Subscription.Status.ACTIVE))
                .thenReturn(Optional.of(previous));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> {
            Subscription s = inv.getArgument(0);
            if (s.getId() == null) s.setId(2L);
            return s;
        });

        var result = paymentService.subscribe(new SubscribeRequest(Subscription.Plan.PREMIUM));

        assertThat(result.plan()).isEqualTo(Subscription.Plan.PREMIUM);
        assertThat(result.status()).isEqualTo(Subscription.Status.ACTIVE);
        assertThat(result.endsAt()).isNotNull();
        assertThat(previous.getStatus()).isEqualTo(Subscription.Status.CANCELLED);
        verify(subscriptionRepository, org.mockito.Mockito.times(2)).save(any(Subscription.class));
    }

    @Test
    void getActiveSubscription_shouldReturnFree_whenNoneActive() {
        when(currentUserProvider.requireId()).thenReturn(1L);
        when(subscriptionRepository.findByUserIdAndStatus(1L, Subscription.Status.ACTIVE))
                .thenReturn(Optional.empty());

        var result = paymentService.getActiveSubscription();

        assertThat(result.plan()).isEqualTo(Subscription.Plan.FREE);
    }
}
