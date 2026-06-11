package com.porraglobal.payments.repository;

import com.porraglobal.payments.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    Optional<Subscription> findByUserIdAndStatus(Long userId, Subscription.Status status);
}
