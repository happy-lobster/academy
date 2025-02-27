package com.ctw.workstation.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<RuntimeException> {
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class ReturnObject {
        public String message;

        private ReturnObject(String message) {
            this.message = message;
        }
    }

    @Override
    public Response toResponse(RuntimeException e) {
        if (e instanceof ConflictingBookingException ||
            e instanceof ConflictingRackException ||
            e instanceof ConflictingTeamException ||
            e instanceof ConflictingTeamMemberException
        ) {
            ReturnObject response = new ReturnObject(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(response).build();
        }

        if (e instanceof InvalidTimeIntervalException ||
            e instanceof RackNotFoundException ||
            e instanceof TeamMemberNotFoundException ||
            e instanceof TeamNotFoundException) {
            ReturnObject response = new ReturnObject(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        Log.error(e.getMessage());
        ReturnObject response = new ReturnObject(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
