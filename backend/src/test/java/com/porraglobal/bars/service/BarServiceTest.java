package com.porraglobal.bars.service;

import com.porraglobal.bars.dto.CreateBarEventRequest;
import com.porraglobal.bars.dto.CreateBarRequest;
import com.porraglobal.bars.entity.Bar;
import com.porraglobal.bars.entity.BarEvent;
import com.porraglobal.bars.mapper.BarMapper;
import com.porraglobal.bars.repository.BarEventRepository;
import com.porraglobal.bars.repository.BarRepository;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.tournament.repository.MatchRepository;
import com.porraglobal.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BarServiceTest {

    @Mock
    private BarRepository barRepository;
    @Mock
    private BarEventRepository barEventRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    private BarService barService;

    private User owner;

    @BeforeEach
    void setUp() {
        var mapper = Mappers.getMapper(BarMapper.class);
        barService = new BarService(barRepository, barEventRepository, matchRepository,
                mapper, currentUserProvider);
        owner = User.builder().id(1L).username("dueno").build();
    }

    @Test
    void createBar_shouldAssignCurrentUserAsOwner() {
        when(currentUserProvider.require()).thenReturn(owner);
        when(barRepository.save(any(Bar.class))).thenAnswer(inv -> {
            Bar b = inv.getArgument(0);
            b.setId(3L);
            return b;
        });

        var request = new CreateBarRequest("Bar Centro", "Buen ambiente", "Calle 1",
                "Madrid", null, null, "600000000");
        var response = barService.createBar(request);

        assertThat(response.id()).isEqualTo(3L);
        assertThat(response.ownerId()).isEqualTo(1L);
        assertThat(response.verified()).isFalse();
    }

    @Test
    void createBarEvent_shouldThrow_whenBarNotFound() {
        when(barRepository.findById(99L)).thenReturn(Optional.empty());
        var request = new CreateBarEventRequest("Final", null,
                Instant.now().plus(1, ChronoUnit.DAYS), null, 50);

        assertThatThrownBy(() -> barService.createBarEvent(99L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBarEvent_shouldPersistEvent_withoutMatch() {
        var bar = Bar.builder().id(3L).name("Bar Centro").owner(owner).build();
        when(barRepository.findById(3L)).thenReturn(Optional.of(bar));
        when(barEventRepository.save(any(BarEvent.class))).thenAnswer(inv -> {
            BarEvent e = inv.getArgument(0);
            e.setId(8L);
            return e;
        });

        var request = new CreateBarEventRequest("Semifinal", "Pantalla gigante",
                Instant.now().plus(2, ChronoUnit.DAYS), null, 80);
        var response = barService.createBarEvent(3L, request);

        assertThat(response.id()).isEqualTo(8L);
        assertThat(response.barId()).isEqualTo(3L);
        assertThat(response.matchId()).isNull();
        assertThat(response.capacity()).isEqualTo(80);
    }

    @Test
    void getBarEvents_shouldThrow_whenBarMissing() {
        when(barRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> barService.getBarEvents(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
