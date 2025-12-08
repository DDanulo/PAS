package com.example.controller.exceptionHandlers;

import com.example.controller.exception.RoomIsReservedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleRoomIsReservedException  implements ExceptionMapper<RoomIsReservedException> {
    @Override
    public Response toResponse(RoomIsReservedException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
