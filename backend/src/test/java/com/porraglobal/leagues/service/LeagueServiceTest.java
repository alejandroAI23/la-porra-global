package com.porraglobal.leagues.service;

import com.porraglobal.common.exception.ConflictException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.leagues.dto.CreateLeagueRequest;
import com.porraglobal.leagues.dto.JoinLeagueRequest;
import com.porraglobal.leagues.entity.League;
import com.porraglobal.leagues.entity.LeagueMember;
import com.porraglobal.leagues.mapper.LeagueMapper;
import com.porraglobal.leagues.repository.LeagueMemberRepository;
import com.porraglobal.leagues.repository.LeagueRepository;
import com.porraglobal.ranking.repository.RankingEntryRepository;
import com.porraglobal.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepository;
    @Mock
    private LeagueMemberRepository leagueMemberRepository;
    @Mock
    private RankingEntryRepository rankingEntryRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    private LeagueService leagueService;

    private User user;

    @BeforeEach
    void setUp() {
        var mapper = Mappers.getMapper(LeagueMapper.class);
        leagueService = new LeagueService(leagueRepository, leagueMemberRepository,
                rankingEntryRepository, mapper, currentUserProvider);
        user = User.builder().id(1L).username("juan").displayName("Juan").build();
    }

    @Test
    void createLeague_shouldPersistLeagueAndOwnerMembership() {
        when(currentUserProvider.require()).thenReturn(user);
        when(leagueRepository.existsByInviteCode(anyString())).thenReturn(false);
        when(leagueRepository.save(any(League.class))).thenAnswer(inv -> {
            League l = inv.getArgument(0);
            l.setId(5L);
            return l;
        });
        when(leagueMemberRepository.save(any(LeagueMember.class))).thenAnswer(inv -> inv.getArgument(0));
        when(leagueMemberRepository.countByLeagueId(5L)).thenReturn(1L);

        var request = new CreateLeagueRequest("Amigos", "Liga de prueba", League.Type.FRIENDS, 10);
        var response = leagueService.createLeague(request);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.inviteCode()).hasSize(6);
        assertThat(response.ownerUsername()).isEqualTo("juan");
        assertThat(response.memberCount()).isEqualTo(1L);
        verify(leagueMemberRepository).save(any(LeagueMember.class));
        verify(rankingEntryRepository).save(any());
    }

    @Test
    void joinLeague_shouldThrow_whenInviteCodeWrong() {
        var league = League.builder().id(5L).inviteCode("ABC123").owner(user).maxMembers(10).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(leagueRepository.findById(5L)).thenReturn(Optional.of(league));

        assertThatThrownBy(() -> leagueService.joinLeague(5L, new JoinLeagueRequest("WRONG1")))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Código");
        verify(leagueMemberRepository, never()).save(any());
    }

    @Test
    void joinLeague_shouldThrow_whenAlreadyMember() {
        var league = League.builder().id(5L).inviteCode("ABC123").owner(user).maxMembers(10).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(leagueRepository.findById(5L)).thenReturn(Optional.of(league));
        when(leagueMemberRepository.existsByLeagueIdAndUserId(5L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> leagueService.joinLeague(5L, new JoinLeagueRequest("ABC123")))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("miembro");
    }

    @Test
    void joinLeague_shouldThrow_whenLeagueFull() {
        var league = League.builder().id(5L).inviteCode("ABC123").owner(user).maxMembers(2).build();
        when(currentUserProvider.require()).thenReturn(user);
        when(leagueRepository.findById(5L)).thenReturn(Optional.of(league));
        when(leagueMemberRepository.existsByLeagueIdAndUserId(5L, 1L)).thenReturn(false);
        when(leagueMemberRepository.countByLeagueId(5L)).thenReturn(2L);

        assertThatThrownBy(() -> leagueService.joinLeague(5L, new JoinLeagueRequest("ABC123")))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("máximo");
    }

    @Test
    void joinLeague_shouldSucceed_whenCodeMatchesAndSpaceAvailable() {
        var league = League.builder().id(5L).inviteCode("ABC123").owner(user).maxMembers(10).build();
        var joiner = User.builder().id(2L).username("ana").displayName("Ana").build();
        when(currentUserProvider.require()).thenReturn(joiner);
        when(leagueRepository.findById(5L)).thenReturn(Optional.of(league));
        when(leagueMemberRepository.existsByLeagueIdAndUserId(5L, 2L)).thenReturn(false);
        when(leagueMemberRepository.countByLeagueId(5L)).thenReturn(1L);
        when(leagueMemberRepository.save(any(LeagueMember.class))).thenAnswer(inv -> {
            LeagueMember m = inv.getArgument(0);
            m.setId(7L);
            return m;
        });

        var response = leagueService.joinLeague(5L, new JoinLeagueRequest("abc123"));

        assertThat(response.userId()).isEqualTo(2L);
        assertThat(response.role()).isEqualTo(LeagueMember.MemberRole.MEMBER);
        verify(rankingEntryRepository).save(any());
    }
}
