package com.ctw.workstation.rack.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RackRequest(
        @Size(max = 30)
        @NotBlank(message="serialNumber may not be blank")
        String serialNumber,
        @Size(max = 11)
        @NotBlank(message="status may not be blank")
        String status,
        @Min(1)
        Long teamId,
        @Size(max = 30)
        @NotBlank(message="defaultLocation may not be blank")
        String defaultLocation
) {}