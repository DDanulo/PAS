package com.example.mappers;

import com.example.domain.*;
import com.example.model.CreateUserDTO;
import com.example.model.ShowUserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User createUserDTOToUser(CreateUserDTO userDTO) {
        switch (userDTO.getUserTypes()) {
            case REGULAR -> {
                return new RegularClient(userDTO.getLogin(),
                        userDTO.getFirstName(),
                        userDTO.getLastName(),
                        userDTO.getEmail());
            }
            case STUDENT -> {
                return new StudentClient(userDTO.getLogin(),
                        userDTO.getFirstName(),
                        userDTO.getLastName(),
                        userDTO.getEmail());
            }
            case TRAINER -> {
                return new TrainerClient(userDTO.getLogin(),
                        userDTO.getFirstName(),
                        userDTO.getLastName(),
                        userDTO.getEmail(),
                        userDTO.getIsTrainerPartner());
            }
            case null, default -> {
                throw new RuntimeException();
            }
        }
    }
    public ShowUserDTO showDTOToUser(Client user) {
        switch (user.getClass()) {
            case RegularClient.class -> {
                RegularClient client = (RegularClient) user;
                return new ShowUserDTO(client.getUserId(),
                        client.getLogin(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getEmail(),
                        client.getLoyaltyCounter(),
                        UserTypes.REGULAR,
                        null);
            }
            case StudentClient.class -> {
                StudentClient client = (StudentClient) user;
                return new ShowUserDTO(client.getUserId(),
                        client.getLogin(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getEmail(),
                        null,
                        UserTypes.STUDENT,
                        null);
            }
            case TrainerClient.class -> {
                TrainerClient client = (TrainerClient) user;
                return new ShowUserDTO(client.getUserId(),
                        client.getLogin(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getEmail(),
                        null,
                        UserTypes.TRAINER,
                        null);
            }
            case null, default -> {
                throw new RuntimeException();
            }
        }
    }

}
