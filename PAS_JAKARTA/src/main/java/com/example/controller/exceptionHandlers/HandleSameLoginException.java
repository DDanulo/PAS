package com.example.controller.exceptionHandlers;

import com.example.controller.exception.SameLoginException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleSameLoginException implements ExceptionMapper<SameLoginException> {

    @Override
    public Response toResponse(SameLoginException e) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
