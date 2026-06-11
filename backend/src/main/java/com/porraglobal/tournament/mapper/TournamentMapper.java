package com.porraglobal.tournament.mapper;

import com.porraglobal.tournament.dto.MatchResponse;
import com.porraglobal.tournament.dto.TeamResponse;
import com.porraglobal.tournament.entity.Match;
import com.porraglobal.tournament.entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    TeamResponse toTeamResponse(Team team);

    MatchResponse toMatchResponse(Match match);
}
