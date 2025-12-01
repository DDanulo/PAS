//package com.example.test.repository;
//
//import com.example.config.MongoConfig;
//import com.example.domain.Client;
//import com.example.domain.Reservation;
//import com.example.domain.Room;
//import com.example.domain.RoomType;
//import com.example.model.users.Role;
//import com.example.repository.ReservationRepository;
//import com.example.repository.RoomRepository;
//import com.example.repository.UserRepository;
//import com.mongodb.client.ClientSession;
//import org.bson.types.ObjectId;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { MongoConfig.class, RoomRepository.class, UserRepository.class, ReservationRepository.class })
//public class ReservationRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RoomRepository roomRepository;
//    @Autowired
//    ReservationRepository reservationRepository;
//    private ClientSession sessionRoom;
//    private ClientSession sessionUser;
//    private ClientSession sessionReservation;
//    private Reservation reservation;
//    private Room room;
//    private Client client;
//
//    @BeforeEach
//    void setUp() {
////        userRepository = new UserRepository();
////        roomRepository = new RoomRepository();
////        reservationRepository = new ReservationRepository();
//        sessionRoom = roomRepository.startSession();
//        sessionUser = userRepository.startSession();
//        sessionReservation = reservationRepository.startSession();
//        reservation = new Reservation(new ObjectId(), room, client, LocalDateTime.now(), 0.);
//        room = new Room(new ObjectId(), RoomType.FIELD, 2, 50.0);
//        client = new Client(new ObjectId(), "client123", "Client", "Test", "ct@gmail.com", true, Role.CLIENT);
//    }
//
//    @AfterEach
//    void tearDown() {
//        Optional.ofNullable(reservation).ifPresent(r -> reservationRepository.remove(sessionReservation, r.getReservationId()));
//    }
//
//    @Test
//    void createReservation() {
//        roomRepository.add(sessionRoom, room);
//        userRepository.add(sessionUser, client);
//
//        reservationRepository.add(sessionReservation, reservation);
//
//        assertEquals(1, reservationRepository.findAll().size());
//    }
//}
