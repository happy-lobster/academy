package com.ctw.workstation.rack.dto;

import com.ctw.workstation.rack.entity.Rack;
import com.ctw.workstation.team.dto.TeamPartialResponse;

public record RackResponse(
    Long id,
    String serialNumber,
    String status,
    TeamPartialResponse team,
    String defaultLocation
) {
    public static RackResponse from(Rack rack) {
        return new RackResponse(
            rack.getId(),
            rack.getSerialNumber(),
            rack.getStatus(),
            TeamPartialResponse.from(rack.getTeam()),
            rack.getDefaultLocation()
        );
    }
}
