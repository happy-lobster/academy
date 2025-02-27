package com.ctw.workstation.team.resource;

import com.ctw.workstation.exception.ConflictingTeamException;
import com.ctw.workstation.team.dto.TeamRequest;
import com.ctw.workstation.team.dto.TeamResponse;
import com.ctw.workstation.team.service.TeamService;
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

@Path(TeamResource.BASE_PATH)
public class TeamResource {
    public static final String BASE_PATH = "/teams";

    @Inject
    TeamService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@RequestBody @Valid TeamRequest request) throws ConflictingTeamException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Creating Team");
        TeamResponse response = service.create(request);
        return Response.ok(response).status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Fetching all Teams");
        List<TeamResponse> teams = service.getAll();
        return Response.ok(teams).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Fetching Team with id: " + id);
        Optional<TeamResponse> response = service.getByIdResponse(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @RequestBody @Valid TeamRequest request) throws ConflictingTeamException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Updating Team with id: " + id);
        Optional<TeamResponse> response = service.update(id, request);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Deleting Team with id: " + id);
        Optional<TeamResponse> response = service.delete(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }
}
