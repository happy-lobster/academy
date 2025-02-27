package com.ctw.workstation.booking.service;

import com.ctw.workstation.booking.dto.BookingRequest;
import com.ctw.workstation.booking.dto.BookingResponse;
import com.ctw.workstation.booking.entity.Booking;
import com.ctw.workstation.booking.repository.BookingRepository;
import com.ctw.workstation.exception.ConflictingBookingException;
import com.ctw.workstation.exception.InvalidTimeIntervalException;
import com.ctw.workstation.exception.RackNotFoundException;
import com.ctw.workstation.exception.TeamMemberNotFoundException;
import com.ctw.workstation.rack.entity.Rack;
import com.ctw.workstation.rack.service.RackService;
import com.ctw.workstation.teammember.entity.TeamMember;
import com.ctw.workstation.teammember.service.TeamMemberService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookingService {
    @Inject
    BookingRepository repo;

    @Inject
    TeamMemberService teamMemberService;

    @Inject
    RackService rackService;

    @Transactional
    public BookingResponse create(BookingRequest request)
        throws TeamMemberNotFoundException, RackNotFoundException, ConflictingBookingException {
        Booking booking = new Booking();
        setBookingFromRequest(booking, request);
        repo.persist(booking);
        repo.flush();
        return BookingResponse.from(booking);
    }

    @Transactional
    public List<BookingResponse> getAll() {
        return repo.findAll().stream().map(BookingResponse::from).toList();
    }

    public Optional<BookingResponse> getByIdResponse(Long id) {
        Optional<Booking> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Booking booking = fetched.get();
        return Optional.of(BookingResponse.from(booking));
    }

    @Transactional
    public Optional<BookingResponse> update(Long id, BookingRequest request) throws IllegalArgumentException {
        Optional<Booking> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Booking booking = fetched.get();
        setBookingFromRequest(booking, request);
        repo.persist(booking);
        repo.flush();
        return Optional.of(BookingResponse.from(booking));
    }

    @Transactional
    public Optional<BookingResponse> delete(Long id) {
        Optional<Booking> fetched = getById(id);
        if (fetched.isEmpty()) return Optional.empty();
        Booking booking = fetched.get();
        repo.delete(booking);
        repo.flush();
        return Optional.of(BookingResponse.from(booking));
    }

    @Transactional
    public Optional<Booking> getById(Long id) {
        return Optional.ofNullable(repo.findById(id));
    }

    private void setBookingFromRequest(Booking booking, BookingRequest request)
            throws TeamMemberNotFoundException, RackNotFoundException, ConflictingBookingException {
        Dependencies dependencies = checkValidOrThrow(request);
        booking.setRack(dependencies.rack);
        booking.setRequester(dependencies.requester);
        booking.setFrom(request.from());
        booking.setTo(request.to());
    }

    private Dependencies checkValidOrThrow(BookingRequest request)
            throws TeamMemberNotFoundException, RackNotFoundException,
            ConflictingBookingException, InvalidTimeIntervalException {
        // Handle requester and rack validity
        Optional<TeamMember> requesterOptional = teamMemberService.getById(request.requesterId());
        Optional<Rack> rackOptional = rackService.getById(request.rackId());
        if (requesterOptional.isEmpty()) throw new TeamMemberNotFoundException(request.requesterId());
        if (rackOptional.isEmpty()) throw new RackNotFoundException(request.rackId());

        TeamMember requester = requesterOptional.get();
        Rack rack = rackOptional.get();

        // Handle time interval validity
        if (request.to().isBefore(request.from())) throw new InvalidTimeIntervalException();
        List<Booking> bookingForRack = repo.getByRack(rack);
        boolean conflicting = bookingForRack.stream().anyMatch(
                booking -> booking.overlaps(request.from(), request.to())
        );
        if (conflicting) throw new ConflictingBookingException("Conflicting bookings");

        return new Dependencies(rack, requester);
    }

    private static class Dependencies {
        Rack rack;
        TeamMember requester;

        private Dependencies(Rack rack, TeamMember requester) {
            this.rack = rack;
            this.requester = requester;
        }
    }
}
