package com.example.controller;

import com.example.controller.exception.AccountNotActiveException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class HandleAccountNotActiveException implements ExceptionMapper<AccountNotActiveException> {
    @Override
    public Response toResponse(AccountNotActiveException exception) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
