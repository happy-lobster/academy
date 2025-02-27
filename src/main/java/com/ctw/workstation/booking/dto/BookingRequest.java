package com.ctw.workstation.booking.dto;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record BookingRequest(
        @Min(1)
        Long rackId,
        @Min(1)
        Long requesterId,
        LocalDateTime from,
        LocalDateTime to
) {
}
