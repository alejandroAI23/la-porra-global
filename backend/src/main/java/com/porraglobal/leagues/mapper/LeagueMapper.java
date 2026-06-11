package com.porraglobal.leagues.mapper;

import com.porraglobal.leagues.dto.LeagueMemberResponse;
import com.porraglobal.leagues.dto.LeagueResponse;
import com.porraglobal.leagues.entity.League;
import com.porraglobal.leagues.entity.LeagueMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "ownerUsername", source = "owner.username")
    @Mapping(target = "memberCount", ignore = true)
    LeagueResponse toResponse(League league);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "displayName", source = "user.displayName")
    LeagueMemberResponse toMemberResponse(LeagueMember member);
}
