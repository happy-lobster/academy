package com.ctw.workstation.teammember.resource;

import com.ctw.workstation.exception.ConflictingTeamMemberException;
import com.ctw.workstation.exception.TeamNotFoundException;
import com.ctw.workstation.teammember.dto.TeamMemberRequest;
import com.ctw.workstation.teammember.dto.TeamMemberResponse;
import com.ctw.workstation.teammember.service.TeamMemberService;
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

@Path(TeamMemberResource.BASE_PATH)
public class TeamMemberResource {
    public static final String BASE_PATH = "/team-members";

    @Inject
    TeamMemberService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@RequestBody @Valid TeamMemberRequest request)
        throws TeamNotFoundException, ConflictingTeamMemberException {
        Log.info("Creating TeamMember");
        TeamMemberResponse response = service.create(request);
        return Response.ok(response).status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Log.info("Fetching all TeamMembers");
        List<TeamMemberResponse> teamMembers = service.getAll();
        return Response.ok(teamMembers).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Log.info("Fetching TeamMember with id: " + id);
        Optional<TeamMemberResponse> response = service.getByIdResponse(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @RequestBody @Valid TeamMemberRequest request)
        throws TeamNotFoundException, ConflictingTeamMemberException {
        Log.info("Updating TeamMember with id: " + id);
        Optional<TeamMemberResponse> response = service.update(id, request);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        Log.info("Deleting TeamMember with id: " + id);
        Optional<TeamMemberResponse> response = service.delete(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }
}
