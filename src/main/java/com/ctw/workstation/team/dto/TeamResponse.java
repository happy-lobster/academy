package com.ctw.workstation.team.dto;

import com.ctw.workstation.rack.dto.RackPartialResponse;
import com.ctw.workstation.team.entity.Team;
import com.ctw.workstation.teammember.dto.TeamMemberPartialResponse;

import java.util.List;

public record TeamResponse(
    Long id,
    String name,
    String product,
    String defaultLocation,
    List<RackPartialResponse> racks,
    List<TeamMemberPartialResponse> members
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
            team.getId(),
            team.getName(),
            team.getProduct(),
            team.getDefaultLocation(),
            team.getRacks().stream().map(RackPartialResponse::from).toList(),
            team.getMembers().stream().map(TeamMemberPartialResponse::from).toList()
        );
    }
}
