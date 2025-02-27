package com.ctw.workstation.teammember.dto;

import com.ctw.workstation.team.dto.TeamPartialResponse;
import com.ctw.workstation.teammember.entity.TeamMember;

public record TeamMemberResponse(
        Long id,
        TeamPartialResponse team,
        String ctwId,
        String name
    ) {
    public static TeamMemberResponse from(TeamMember teamMember) {
        return new TeamMemberResponse(
                teamMember.getId(),
                TeamPartialResponse.from(teamMember.getTeam()),
                teamMember.getCtwId(),
                teamMember.getName()
        );
    }
}
