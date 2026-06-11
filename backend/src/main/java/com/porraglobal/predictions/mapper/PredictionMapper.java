package com.porraglobal.predictions.mapper;

import com.porraglobal.predictions.dto.PredictionResponse;
import com.porraglobal.predictions.entity.Prediction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PredictionMapper {

    @Mapping(target = "matchId", source = "match.id")
    PredictionResponse toResponse(Prediction prediction);
}
