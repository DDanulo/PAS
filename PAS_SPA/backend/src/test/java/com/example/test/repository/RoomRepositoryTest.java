//package com.example.test.repository;
//
//import com.example.config.MongoConfig;
//import com.example.domain.Room;
//import com.example.domain.RoomType;
//import com.example.repository.RoomRepository;
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
//import java.util.List;
//import java.util.Optional;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { MongoConfig.class, RoomRepository.class })
//public class RoomRepositoryTest {
//
//    @Autowired
//    private RoomRepository roomRepository;
//    private ClientSession session;
//    private Room testroom;
//
//    @BeforeEach
//    public void setUp() {
////        roomRepository = new RoomRepository();
//        session = roomRepository.startSession();
//
//        testroom = new Room(new ObjectId(), RoomType.FIELD, 5,90.0);
//        roomRepository.add(session, testroom);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        Optional.ofNullable(testroom).ifPresent(r -> roomRepository.remove(session, r.getRoomId()));
//    }
//
//    @Test
//    public void createAllRooms() {
//        Room room1 = new Room(new ObjectId(), RoomType.FIELD,2,50.0);
//        Room room2 = new Room(new ObjectId(),RoomType.GYM,35,30.0);
//        Room room3 = new Room(new ObjectId(),RoomType.HALL,40,150.0);
//
//        roomRepository.add(session, room1);
//        roomRepository.add(session, room2);
//        roomRepository.add(session, room3);
//        System.out.println(roomRepository.findAll());
//
//        List<Room> roomNow = roomRepository.findAll();
//
//        assertAll(
//                () -> assertNotNull(roomNow),
//                () -> assertEquals(2, roomRepository.findById(room1.getRoomId()).get().getCapacity()),
//                () -> assertEquals(35, roomRepository.findById(room2.getRoomId()).get().getCapacity()),
//                () -> assertEquals(40, roomRepository.findById(room3.getRoomId()).get().getCapacity()),
//                () -> assertEquals(50.0, roomRepository.findById(room1.getRoomId()).get().getBasePrice()),
//                () -> assertEquals(30.0, roomRepository.findById(room2.getRoomId()).get().getBasePrice()),
//                () -> assertEquals(150.0, roomRepository.findById(room3.getRoomId()).get().getBasePrice()),
//                () -> assertEquals(RoomType.FIELD, roomRepository.findById(room1.getRoomId()).get().getRoomType()),
//                () -> assertEquals(RoomType.GYM, roomRepository.findById(room2.getRoomId()).get().getRoomType()),
//                () -> assertEquals(RoomType.HALL, roomRepository.findById(room3.getRoomId()).get().getRoomType())
//        );
//    }
//
//    @Test
//    public void updateRoomType() {
//        assertEquals(RoomType.FIELD, roomRepository.findById(testroom.getRoomId()).get().getRoomType());
//
//        testroom.setRoomType(RoomType.GYM);
//        roomRepository.update(session,testroom.getRoomId(),testroom);
//
//        assertEquals(RoomType.GYM, roomRepository.findById(testroom.getRoomId()).get().getRoomType());
//    }
//
//    @Test
//    public void updateRoomCapacity() {
//        assertEquals(5, roomRepository.findById(testroom.getRoomId()).get().getCapacity());
//
//        testroom.setCapacity(13);
//        roomRepository.update(session,testroom.getRoomId(),testroom);
//
//        assertEquals(13, roomRepository.findById(testroom.getRoomId()).get().getCapacity());
//    }
//
//    @Test
//    public void updateRoomBasePrice() {
//        assertEquals(90.0, roomRepository.findById(testroom.getRoomId()).get().getBasePrice());
//
//        testroom.setBasePrice(50.0);
//        roomRepository.update(session,testroom.getRoomId(),testroom);
//
//        assertEquals(50.0, roomRepository.findById(testroom.getRoomId()).get().getBasePrice());
//    }
//
//    @Test
//    public void findByRoomId() {
//        assertNotNull(roomRepository.findById(testroom.getRoomId()));
//
//        Optional<Room> roomHolder = roomRepository.findById(testroom.getRoomId());
//
//        assertNotNull(roomHolder);
//    }
//
//    @Test
//    public void findAll() {
//        Room room1 = new Room(new ObjectId(), RoomType.FIELD,27,50.0);
//        roomRepository.add(session, room1);
//        List<Room> roomNow = roomRepository.findAll();
//
//        List<Integer> roomCapacity = roomNow.stream().map(Room::getCapacity).toList();
//        assertAll(
//                () -> assertNotNull(roomNow),
//                () -> assertTrue(roomCapacity.contains(27)),
//                () -> assertTrue(roomCapacity.contains(5))
//        );
//    }
//
//    @Test
//    public void deleteRoom() {
//        Room roomToDelete = new Room(new ObjectId(), RoomType.FIELD,32,55.0);
//        roomRepository.add(session, roomToDelete);
//
//        List<Room> roomNow = roomRepository.findAll();
//        List<Integer> roomCapacity = roomNow.stream().map(Room::getCapacity).toList();
//
//        assertTrue(roomCapacity.contains(32));
//
//        roomRepository.remove(session,roomToDelete.getRoomId());
//
//        roomNow = roomRepository.findAll();
//        roomCapacity = roomNow.stream().map(Room::getCapacity).toList();
//        assertFalse(roomCapacity.contains(32));
//    }
//}
