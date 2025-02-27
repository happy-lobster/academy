package com.ctw.workstation.booking.dto;

import com.ctw.workstation.booking.entity.Booking;
import com.ctw.workstation.rack.dto.RackResponse;
import com.ctw.workstation.teammember.dto.TeamMemberResponse;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        RackResponse rack,
        TeamMemberResponse requester,
        LocalDateTime from,
        LocalDateTime to
) {
    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                RackResponse.from(booking.getRack()),
                TeamMemberResponse.from(booking.getRequester()),
                booking.getFrom(),
                booking.getTo()
        );
    }
}
