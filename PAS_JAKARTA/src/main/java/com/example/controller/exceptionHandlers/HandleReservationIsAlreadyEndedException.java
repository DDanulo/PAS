package com.example.controller.exceptionHandlers;

import com.example.controller.exception.ReservationHasEndedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleReservationIsAlreadyEndedException implements ExceptionMapper<ReservationHasEndedException> {


    @Override
    public Response toResponse(ReservationHasEndedException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
