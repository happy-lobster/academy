package com.ctw.workstation.rack.resource;

import com.ctw.workstation.exception.ConflictingRackException;
import com.ctw.workstation.exception.TeamNotFoundException;
import com.ctw.workstation.rack.dto.RackRequest;
import com.ctw.workstation.rack.dto.RackResponse;
import com.ctw.workstation.rack.service.RackService;
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

@Path(RackResource.BASE_PATH)
public class RackResource {
    public static final String BASE_PATH = "/racks";

    @Inject
    RackService rackService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@RequestBody @Valid RackRequest request)
        throws TeamNotFoundException, ConflictingRackException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Creating Rack");
        RackResponse response = rackService.create(request);
        return Response.ok(response).status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Fetching all Racks");
        List<RackResponse> responses = rackService.getAll();
        return Response.ok(responses).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Fetching Rack with id " + id);
        Optional<RackResponse> response = rackService.getByIdResponse(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{serialNumber}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBySerialNumber(@PathParam("serialNumber") String serialNumber) {
        Log.info("Fetching Rack with serial number " + serialNumber);
        Optional<RackResponse> response = rackService.getBySerialNumberResponse(serialNumber);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @RequestBody @Valid RackRequest request)
        throws TeamNotFoundException, ConflictingRackException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Updating Rack with id " + id);
        Optional<RackResponse> response = rackService.update(id, request);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Deleting Rack with id " + id);
        Optional<RackResponse> response = rackService.delete(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }
}
