package com.ctw.workstation.booking.resource;

import com.ctw.workstation.booking.repository.BookingRepository;
import com.ctw.workstation.team.repository.TeamRepository;
import com.ctw.workstation.teammember.repository.TeamMemberRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(BookingResource.class)
class BookingResourceTest {
    @Inject
    TeamRepository teamRepository;

    @Inject
    TeamMemberRepository teamMemberRepository;

    @Inject
    BookingRepository bookingRepository;

    @BeforeEach
    void setup() {}

    @AfterEach
    void teardown() {}

    @Test
    @DisplayName("Create valid Booking")
    void createValidBooking() {}

    @Test
    @DisplayName("Create Booking for not existing Rack")
    void createBookingForNotExistingRack() {}

    @Test
    @DisplayName("Create Booking by not existing TeamMember")
    void createBookingForNotExistingTeamMember() {}

    @Test
    @DisplayName("Create conflicting Booking")
    void createConflictingBooking() {}

    @Test
    @DisplayName("Fetch all Bookings")
    void fetchAllBookings() {}

    @Test
    @DisplayName("Fetch Booking by id")
    void fetchBookingById() {}

    @Test
    @DisplayName("Fetch not exiting Booking by id")
    void fetchNotExitingBookingById() {}

    @Test
    @DisplayName("Update Booking with valid fields")
    void updateBookingWithValidFields() {}

    @Test
    @DisplayName("Update Booking for not existing Rack")
    void updateBookingForNotExistingRack() {}

    @Test
    @DisplayName("Update Booking by not existing TeamMember")
    void updateBookingForNotExistingTeamMember() {}

    @Test
    @DisplayName("Update Booking with conflicting fields")
    void updateBookingForConflictingFields() {}

    @Test
    @DisplayName("Update not existing Booking")
    void updateNotExistingBooking() {}

    @Test
    @DisplayName("Delete Booking")
    void deleteBooking() {}

    @Test
    @DisplayName("Delete not existing Booking")
    void deleteNotExistingBooking() {}
}