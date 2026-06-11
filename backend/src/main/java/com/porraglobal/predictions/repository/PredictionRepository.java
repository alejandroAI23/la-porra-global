package com.porraglobal.predictions.repository;

import com.porraglobal.predictions.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    Optional<Prediction> findByUserIdAndMatchId(Long userId, Long matchId);

    List<Prediction> findByUserId(Long userId);

    List<Prediction> findByMatchId(Long matchId);
}
