package com.example.controller;

import com.example.controller.exception.NotFoundException;
import com.example.model.users.CreateAdminDTO;
import com.example.model.users.CreateClientDTO;
import com.example.model.users.CreateModeratorDTO;
import com.example.model.users.ShowUserDTO;
import com.example.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    public List<ShowUserDTO> getAllClients() {
        return userService.getAllUsers();
    }

    @POST
    @Path("/client")
    public void createClient(@Valid CreateClientDTO userDTO) {
        userService.registerClient(userDTO);
    }

    @POST
    @Path("/admin")
    public void createAdmin(@Valid CreateAdminDTO userDTO) {
        userService.registerAdmin(userDTO);
    }

    @POST
    @Path("/moderator")
    public void createModerator(@Valid CreateModeratorDTO userDTO) {
        userService.registerModerator(userDTO);
    }

    @GET
    @Path("/{id}")
    public ShowUserDTO getClientById(@PathParam("id") String id) {
        return (userService.findUser(id).orElseThrow(NotFoundException::new));
    }

    @PUT
    @Path("/{id}")
    public void updateClient(@PathParam("id") String id,
                             @Valid CreateClientDTO userDTO) {
        userService.updateClient(id, userDTO);
    }

    @GET
    @Path("/by-login/{login}")
    public ShowUserDTO getClientByLogin(@PathParam("login") String login) {
        return userService.getClientByLogin(login).orElseThrow(NotFoundException::new);
    }

    @GET
    @Path("/search")
    public List<ShowUserDTO> findClientsByLogin(@QueryParam("login") String login) {
        return userService.findClientsByLogin(login);
    }

    @POST
    @Path("/{id}/activate")
    public void activateClient(@PathParam("id") String id) {
        userService.activateClient(id);
    }

    @POST
    @Path("/{id}/deactivate")
    public void deactivateClient(@PathParam("id") String id) {
        userService.deactivateClient(id);
    }

}
