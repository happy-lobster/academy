package com.ctw.workstation.teammember.dto;

import com.ctw.workstation.teammember.entity.TeamMember;

public record TeamMemberPartialResponse(
        Long id,
        String ctwId,
        String name
) {
    public static TeamMemberPartialResponse from(TeamMember teamMember) {
        return new TeamMemberPartialResponse(teamMember.getId(), teamMember.getCtwId(), teamMember.getName());
    }
}
