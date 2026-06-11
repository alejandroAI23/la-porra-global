package com.porraglobal.bars.mapper;

import com.porraglobal.bars.dto.BarEventResponse;
import com.porraglobal.bars.dto.BarResponse;
import com.porraglobal.bars.entity.Bar;
import com.porraglobal.bars.entity.BarEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BarMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    BarResponse toResponse(Bar bar);

    @Mapping(target = "barId", source = "bar.id")
    @Mapping(target = "matchId", source = "match.id")
    BarEventResponse toEventResponse(BarEvent event);
}
