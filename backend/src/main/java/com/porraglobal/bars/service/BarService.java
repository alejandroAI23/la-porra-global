package com.porraglobal.bars.service;

import com.porraglobal.bars.dto.BarEventResponse;
import com.porraglobal.bars.dto.BarResponse;
import com.porraglobal.bars.dto.CreateBarEventRequest;
import com.porraglobal.bars.dto.CreateBarRequest;
import com.porraglobal.bars.entity.Bar;
import com.porraglobal.bars.entity.BarEvent;
import com.porraglobal.bars.mapper.BarMapper;
import com.porraglobal.bars.repository.BarEventRepository;
import com.porraglobal.bars.repository.BarRepository;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.repository.MatchRepository;
import com.porraglobal.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarService {

    private final BarRepository barRepository;
    private final BarEventRepository barEventRepository;
    private final MatchRepository matchRepository;
    private final BarMapper barMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public List<BarResponse> getBars(String city) {
        List<Bar> bars = StringUtils.hasText(city)
                ? barRepository.findByCityIgnoreCase(city)
                : barRepository.findAll();
        return bars.stream().map(barMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BarResponse getBar(Long id) {
        return barRepository.findById(id)
                .map(barMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Bar", id));
    }

    @Transactional
    public BarResponse createBar(CreateBarRequest request) {
        User owner = currentUserProvider.require();
        Bar bar = Bar.builder()
                .name(request.name())
                .description(request.description())
                .address(request.address())
                .city(request.city())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .phone(request.phone())
                .owner(owner)
                .build();
        return barMapper.toResponse(barRepository.save(bar));
    }

    @Transactional(readOnly = true)
    public List<BarEventResponse> getBarEvents(Long barId) {
        if (!barRepository.existsById(barId)) {
            throw new ResourceNotFoundException("Bar", barId);
        }
        return barEventRepository.findByBarIdOrderByStartsAtAsc(barId).stream()
                .map(barMapper::toEventResponse)
                .toList();
    }

    @Transactional
    public BarEventResponse createBarEvent(Long barId, CreateBarEventRequest request) {
        Bar bar = barRepository.findById(barId)
                .orElseThrow(() -> new ResourceNotFoundException("Bar", barId));

        Match match = null;
        if (request.matchId() != null) {
            match = matchRepository.findById(request.matchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Partido", request.matchId()));
        }

        BarEvent event = BarEvent.builder()
                .bar(bar)
                .match(match)
                .title(request.title())
                .description(request.description())
                .startsAt(request.startsAt())
                .capacity(request.capacity())
                .build();
        return barMapper.toEventResponse(barEventRepository.save(event));
    }
}
