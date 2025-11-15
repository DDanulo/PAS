package com.example.mappers;

import com.example.domain.*;
import com.example.model.CreateUserDTO;
import com.example.model.ShowUserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User createUserDTOToClient(CreateUserDTO userDTO) {
        return new Client(userDTO.getLogin(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
    }

    public ShowUserDTO showDTOToClient(Client user) {
        return new ShowUserDTO(user.getUserId(),user.getLogin(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

}
