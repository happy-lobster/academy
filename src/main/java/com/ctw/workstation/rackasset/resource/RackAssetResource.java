package com.ctw.workstation.rackasset.resource;

import com.ctw.workstation.exception.RackNotFoundException;
import com.ctw.workstation.rackasset.dto.RackAssetRequest;
import com.ctw.workstation.rackasset.dto.RackAssetResponse;
import com.ctw.workstation.rackasset.service.RackAssetService;
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

@Path(RackAssetResource.BASE_PATH)
public class RackAssetResource {
    public static final String BASE_PATH = "/rack-assets";

    @Inject
    RackAssetService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@RequestBody @Valid RackAssetRequest request) throws RackNotFoundException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Creating RackAsset");
        RackAssetResponse response = service.create(request);
        return Response.ok(response).status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH);
        Log.info("Fetching all Rack Assets");
        List<RackAssetResponse> response = service.getAll();
        return Response.ok(response).status(Response.Status.OK).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Fetching Rack Asset with id: " + id);
        Optional<RackAssetResponse> response = service.getByIdResponse(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, @RequestBody @Valid RackAssetRequest request) throws RackNotFoundException {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Updating RackAsset with id: " + id);
        Optional<RackAssetResponse> response = service.update(id, request);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        MDC.put("request.id", UUID.randomUUID().toString());
        MDC.put("request.path", BASE_PATH + "/" + id.toString());
        Log.info("Deleting RackAsset with id: " + id);
        Optional<RackAssetResponse> response = service.delete(id);
        if (response.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(response.get()).build();
    }
}
