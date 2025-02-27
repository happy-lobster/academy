package com.ctw.workstation.booking.resource;

import com.ctw.workstation.booking.dto.BookingRequest;
import com.ctw.workstation.booking.dto.BookingResponse;
import com.ctw.workstation.booking.service.BookingService;
import com.ctw.workstation.exception.ConflictingBookingException;
import com.ctw.workstation.exception.InvalidTimeIntervalException;
import com.ctw.workstation.exception.RackNotFoundException;
import com.ctw.workstation.exception.TeamMemberNotFoundException;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.logmanager.MDC;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path(BookingResource.BASE_PATH)
public class BookingResource {
    public static final String BASE_PATH = "/bookings";

    @Inject
    BookingService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@RequestBody @Valid BookingRequest request)
            throws RackNotFoundException, TeamMemberNotFoundException,
            InvalidTimeIntervalException, ConflictingBookingException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Creating Booking");
        BookingResponse response = service.create(request);
        return Response.ok(response).status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Fetching all Bookings");
        List<BookingResponse> bookings = service.getAll();
        return Response.ok(bookings).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Fetching Booking with id " + id);
        Optional<BookingResponse> response = service.getByIdResponse(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @RequestBody @Valid BookingRequest request)
        throws RackNotFoundException, TeamMemberNotFoundException,
        InvalidTimeIntervalException, ConflictingBookingException{
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Updating Booking with id " + id);
        Optional<BookingResponse> response = service.update(id, request);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Deleting Booking with id " + id);
        Optional<BookingResponse> response = service.delete(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }
}
