package com.example.controller;

import com.example.domain.Client;
import com.example.model.ShowRoomDTO;
import com.example.model.ShowUserDTO;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public List<ShowUserDTO> getAllRooms(){
        return clientService.getAllUsers();
    }

}
