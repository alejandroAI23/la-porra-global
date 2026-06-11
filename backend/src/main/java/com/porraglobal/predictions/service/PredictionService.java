package com.porraglobal.predictions.service;

import com.porraglobal.common.exception.BadRequestException;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.predictions.dto.CreatePredictionRequest;
import com.porraglobal.predictions.dto.PredictionResponse;
import com.porraglobal.predictions.entity.Prediction;
import com.porraglobal.predictions.mapper.PredictionMapper;
import com.porraglobal.predictions.repository.PredictionRepository;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.repository.MatchRepository;
import com.porraglobal.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final MatchRepository matchRepository;
    private final PredictionMapper predictionMapper;
    private final CurrentUserProvider currentUserProvider;

    /**
     * Crea o actualiza la predicción del usuario para un partido.
     * Las predicciones se cierran en el pitido inicial (kickoff).
     */
    @Transactional
    public PredictionResponse submit(CreatePredictionRequest request) {
        User user = currentUserProvider.require();
        Match match = matchRepository.findById(request.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Partido", request.matchId()));

        if (!Instant.now().isBefore(match.getKickoffAt())) {
            throw new BadRequestException("Las predicciones se han cerrado: el partido ya ha comenzado");
        }
        if (match.getStatus() != Match.Status.SCHEDULED) {
            throw new BadRequestException("Solo se pueden predecir partidos programados");
        }

        Prediction prediction = predictionRepository
                .findByUserIdAndMatchId(user.getId(), match.getId())
                .orElseGet(() -> Prediction.builder().user(user).match(match).build());

        boolean isUpdate = prediction.getId() != null;
        prediction.setPredictedHomeScore(request.homeScore());
        prediction.setPredictedAwayScore(request.awayScore());
        if (isUpdate) {
            prediction.setUpdatedAt(Instant.now());
        }

        return predictionMapper.toResponse(predictionRepository.save(prediction));
    }

    @Transactional(readOnly = true)
    public List<PredictionResponse> getMyPredictions() {
        Long userId = currentUserProvider.requireId();
        return predictionRepository.findByUserId(userId).stream()
                .map(predictionMapper::toResponse)
                .toList();
    }
}
