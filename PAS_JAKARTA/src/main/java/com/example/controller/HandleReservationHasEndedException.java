package com.example.controller;

import com.example.controller.exception.ReservationHasEndedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleReservationHasEndedException implements ExceptionMapper<ReservationHasEndedException> {
    @Override
    public Response toResponse(ReservationHasEndedException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
