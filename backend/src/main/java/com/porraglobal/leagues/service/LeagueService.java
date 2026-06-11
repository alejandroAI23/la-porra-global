package com.porraglobal.leagues.service;

import com.porraglobal.common.exception.ConflictException;
import com.porraglobal.common.exception.ResourceNotFoundException;
import com.porraglobal.common.security.CurrentUserProvider;
import com.porraglobal.leagues.dto.CreateLeagueRequest;
import com.porraglobal.leagues.dto.JoinLeagueRequest;
import com.porraglobal.leagues.dto.LeagueMemberResponse;
import com.porraglobal.leagues.dto.LeagueResponse;
import com.porraglobal.leagues.entity.League;
import com.porraglobal.leagues.entity.LeagueMember;
import com.porraglobal.leagues.mapper.LeagueMapper;
import com.porraglobal.leagues.repository.LeagueMemberRepository;
import com.porraglobal.leagues.repository.LeagueRepository;
import com.porraglobal.ranking.entity.RankingEntry;
import com.porraglobal.ranking.repository.RankingEntryRepository;
import com.porraglobal.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private static final String CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final LeagueRepository leagueRepository;
    private final LeagueMemberRepository leagueMemberRepository;
    private final RankingEntryRepository rankingEntryRepository;
    private final LeagueMapper leagueMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public LeagueResponse createLeague(CreateLeagueRequest request) {
        User owner = currentUserProvider.require();

        League league = League.builder()
                .name(request.name())
                .description(request.description())
                .inviteCode(generateUniqueInviteCode())
                .type(request.type() != null ? request.type() : League.Type.FRIENDS)
                .owner(owner)
                .maxMembers(request.maxMembers() != null ? request.maxMembers() : 50)
                .build();
        league = leagueRepository.save(league);

        addMember(league, owner, LeagueMember.MemberRole.OWNER);
        return toResponse(league);
    }

    @Transactional
    public LeagueMemberResponse joinLeague(Long leagueId, JoinLeagueRequest request) {
        User user = currentUserProvider.require();
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new ResourceNotFoundException("Liga", leagueId));

        if (!league.getInviteCode().equalsIgnoreCase(request.inviteCode())) {
            throw new ConflictException("Código de invitación incorrecto");
        }
        if (leagueMemberRepository.existsByLeagueIdAndUserId(leagueId, user.getId())) {
            throw new ConflictException("Ya eres miembro de esta liga");
        }
        if (leagueMemberRepository.countByLeagueId(leagueId) >= league.getMaxMembers()) {
            throw new ConflictException("La liga ha alcanzado su número máximo de miembros");
        }

        LeagueMember member = addMember(league, user, LeagueMember.MemberRole.MEMBER);
        return leagueMapper.toMemberResponse(member);
    }

    @Transactional(readOnly = true)
    public LeagueResponse getLeague(Long leagueId) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new ResourceNotFoundException("Liga", leagueId));
        return toResponse(league);
    }

    @Transactional(readOnly = true)
    public List<LeagueMemberResponse> getMembers(Long leagueId) {
        if (!leagueRepository.existsById(leagueId)) {
            throw new ResourceNotFoundException("Liga", leagueId);
        }
        return leagueMemberRepository.findByLeagueId(leagueId).stream()
                .map(leagueMapper::toMemberResponse)
                .toList();
    }

    private LeagueMember addMember(League league, User user, LeagueMember.MemberRole role) {
        LeagueMember member = leagueMemberRepository.save(LeagueMember.builder()
                .league(league)
                .user(user)
                .role(role)
                .build());

        rankingEntryRepository.save(RankingEntry.builder()
                .league(league)
                .user(user)
                .build());
        return member;
    }

    private LeagueResponse toResponse(League league) {
        LeagueResponse base = leagueMapper.toResponse(league);
        long memberCount = leagueMemberRepository.countByLeagueId(league.getId());
        return new LeagueResponse(base.id(), base.name(), base.description(), base.inviteCode(),
                base.type(), base.ownerId(), base.ownerUsername(), base.maxMembers(),
                memberCount, base.createdAt());
    }

    private String generateUniqueInviteCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_ALPHABET.charAt(RANDOM.nextInt(CODE_ALPHABET.length())));
            }
            code = sb.toString();
        } while (leagueRepository.existsByInviteCode(code));
        return code;
    }
}
