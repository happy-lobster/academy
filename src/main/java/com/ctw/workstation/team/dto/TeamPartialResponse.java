package com.ctw.workstation.team.dto;

import com.ctw.workstation.team.entity.Team;

public record TeamPartialResponse(
        Long id,
        String name
) {
    public static TeamPartialResponse from(Team team) {
        return new TeamPartialResponse(team.getId(), team.getName());
    }
}
