package com.ctw.workstation.rack.dto;

import com.ctw.workstation.rack.entity.Rack;

public record RackPartialResponse(
        Long id,
        String serialNumber,
        String status,
        String defaultLocation
) {
    public static RackPartialResponse from(Rack rack) {
        return new RackPartialResponse(rack.getId(), rack.getSerialNumber(), rack.getStatus(), rack.getDefaultLocation());
    }
}
