package com.example.test.repository;

import com.example.domain.Admin;
import com.example.domain.Client;
import com.example.domain.Moderator;
import com.example.domain.User;
import com.example.model.users.Role;
import com.example.repository.UserRepository;
import com.mongodb.client.ClientSession;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
//
//    UserRepository userRepository;
//    private ClientSession session;
//    private Client clientToUpdate;
//    private Moderator moderatorToUpdate;
//    private Admin adminToUpdate;
//
//    @BeforeEach
//    void setUp() {
//        userRepository = new UserRepository();
//        session = userRepository.startSession();
//
//        clientToUpdate = new Client(new ObjectId(), "client123update", "ClientUpdate", "TestUpdate", "ctupdate@gmail.com", true, Role.CLIENT);
//        adminToUpdate = new Admin(new ObjectId(), "admin123update", "AdminUpdate", "TestUpdate", "atupdate@gmail.com", true, Role.ADMIN);
//        moderatorToUpdate = new Moderator(new ObjectId(), "moderator123update", "ModeratorUpdate", "TestUpdate", "mtupdate@gmail.com", true, Role.MODERATOR);
//
//        userRepository.add(session, clientToUpdate);
//        userRepository.add(session, adminToUpdate);
//        userRepository.add(session, moderatorToUpdate);
//    }
//
//    @AfterEach
//    void tearDown() {
//        Optional.ofNullable(clientToUpdate).ifPresent(c -> userRepository.remove(session, c.getUserId()));
//        Optional.ofNullable(adminToUpdate).ifPresent(a -> userRepository.remove(session, a.getUserId()));
//        Optional.ofNullable(moderatorToUpdate).ifPresent(m -> userRepository.remove(session, m.getUserId()));
//    }
//
//    @Test
//    public void testClientCreate() {
//        Client client = new Client(new ObjectId(), "client123", "Client", "Test", "ct@gmail.com", true, Role.CLIENT);
//
//        try {
//            userRepository.add(session, client);
//
//            Optional<User> clientNow = userRepository.findById(client.getUserId());
//
//            assertAll(
//                    () -> assertTrue(clientNow.isPresent()),
//                    () -> assertEquals(client.getUserId(), clientNow.get().getUserId()),
//                    () -> assertEquals("client123", clientNow.get().getLogin()),
//                    () -> assertEquals("Client", clientNow.get().getFirstName()),
//                    () -> assertEquals("Test", clientNow.get().getLastName()),
//                    () -> assertEquals(Role.CLIENT, clientNow.get().getRole()),
//                    () -> assertEquals("ct@gmail.com", clientNow.get().getEmail())
//            );
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testAdminCreate() {
//        Admin admin = new Admin(new ObjectId(), "admin123", "Admin", "Test", "at@gmail.com", true, Role.ADMIN);
//
//        try {
//            userRepository.add(session, admin);
//
//            Optional<User> adminNow = userRepository.findById(admin.getUserId());
//
//            assertAll(
//                    () -> assertTrue(adminNow.isPresent()),
//                    () -> assertEquals(admin.getUserId(), adminNow.get().getUserId()),
//                    () -> assertEquals("admin123", adminNow.get().getLogin()),
//                    () -> assertEquals("Admin", adminNow.get().getFirstName()),
//                    () -> assertEquals("Test", adminNow.get().getLastName()),
//                    () -> assertEquals(Role.ADMIN, adminNow.get().getRole()),
//                    () -> assertEquals("at@gmail.com", adminNow.get().getEmail())
//            );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testModeratorCreate() {
//        Moderator moderator = new Moderator(new ObjectId(), "moderator123", "Moderator", "Test", "mt@gmail.com", true, Role.MODERATOR);
//
//        try {
//            userRepository.add(session, moderator);
//
//            Optional<User> moderatorNow = userRepository.findById(moderator.getUserId());
//
//            assertAll(
//                    () -> assertTrue(moderatorNow.isPresent()),
//                    () -> assertEquals(moderator.getUserId(), moderatorNow.get().getUserId()),
//                    () -> assertEquals("moderator123", moderatorNow.get().getLogin()),
//                    () -> assertEquals("Moderator", moderatorNow.get().getFirstName()),
//                    () -> assertEquals("Test", moderatorNow.get().getLastName()),
//                    () -> assertEquals(Role.MODERATOR, moderatorNow.get().getRole()),
//                    () -> assertEquals("mt@gmail.com", moderatorNow.get().getEmail())
//            );
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testClientUpdateLogin() {
//        assertTrue("client123update".equals(clientToUpdate.getLogin()));
//
//        clientToUpdate.setLogin("321client");
//        userRepository.update(session, clientToUpdate.getUserId(), clientToUpdate);
//
//        assertEquals("321client", clientToUpdate.getLogin());
//    }
//
//    @Test
//    public void testClientUpdateFirstName() {
//        assertTrue("ClientUpdate".equals(clientToUpdate.getFirstName()));
//
//        clientToUpdate.setFirstName("ClientName");
//        userRepository.update(session, clientToUpdate.getUserId(), clientToUpdate);
//
//        assertEquals("ClientName", clientToUpdate.getFirstName());
//    }
//
//    @Test
//    public void testClientUpdateLastName() {
//        assertTrue("TestUpdate".equals(clientToUpdate.getLastName()));
//
//        clientToUpdate.setLastName("ClientLastName");
//        userRepository.update(session, clientToUpdate.getUserId(), clientToUpdate);
//
//        assertEquals("ClientLastName", clientToUpdate.getLastName());
//    }
//
//    @Test
//    public void testClientUpdateEmail() {
//        assertTrue("ctupdate@gmail.com".equals(clientToUpdate.getEmail()));
//
//        clientToUpdate.setEmail("newclient@gmail.com");
//        userRepository.update(session, clientToUpdate.getUserId(), clientToUpdate);
//
//        assertEquals("newclient@gmail.com", clientToUpdate.getEmail());
//    }
//
//    @Test
//    public void testAdminUpdateLogin() {
//        assertTrue("admin123update".equals(adminToUpdate.getLogin()));
//
//        adminToUpdate.setLogin("321admin");
//        userRepository.update(session, adminToUpdate.getUserId(), adminToUpdate);
//
//        assertEquals("321admin", adminToUpdate.getLogin());
//    }
//
//    @Test
//    public void testAdminUpdateFirstName() {
//        assertTrue("AdminUpdate".equals(adminToUpdate.getFirstName()));
//
//        adminToUpdate.setFirstName("AdminName");
//        userRepository.update(session, adminToUpdate.getUserId(), adminToUpdate);
//
//        assertEquals("AdminName", adminToUpdate.getFirstName());
//    }
//
//    @Test
//    public void testAdminUpdateLastName() {
//        assertTrue("TestUpdate".equals(adminToUpdate.getLastName()));
//
//        adminToUpdate.setLastName("AdminLastName");
//        userRepository.update(session, adminToUpdate.getUserId(), adminToUpdate);
//
//        assertEquals("AdminLastName", adminToUpdate.getLastName());
//    }
//
//    @Test
//    public void testAdminUpdateEmail() {
//        assertTrue("atupdate@gmail.com".equals(adminToUpdate.getEmail()));
//
//        adminToUpdate.setEmail("newadmin@gmail.com");
//        userRepository.update(session, adminToUpdate.getUserId(), adminToUpdate);
//
//        assertEquals("newadmin@gmail.com", adminToUpdate.getEmail());
//    }
//
//    @Test
//    public void testModeratorUpdateLogin() {
//        assertTrue("moderator123update".equals(moderatorToUpdate.getLogin()));
//
//        moderatorToUpdate.setLogin("321moderator");
//        userRepository.update(session, moderatorToUpdate.getUserId(), moderatorToUpdate);
//
//        assertEquals("321moderator", moderatorToUpdate.getLogin());
//    }
//
//    @Test
//    public void testModeratorUpdateFirstName() {
//        assertTrue("ModeratorUpdate".equals(moderatorToUpdate.getFirstName()));
//
//        moderatorToUpdate.setFirstName("ModeratorName");
//        userRepository.update(session, moderatorToUpdate.getUserId(), moderatorToUpdate);
//
//        assertEquals("ModeratorName", moderatorToUpdate.getFirstName());
//    }
//
//    @Test
//    public void testModeratorUpdateLastName() {
//        assertTrue("TestUpdate".equals(moderatorToUpdate.getLastName()));
//
//        moderatorToUpdate.setLastName("ModeratorLastName");
//        userRepository.update(session, moderatorToUpdate.getUserId(), moderatorToUpdate);
//
//        assertEquals("ModeratorLastName", moderatorToUpdate.getLastName());
//    }
//
//    @Test
//    public void testModeratorUpdateEmail() {
//        assertTrue("mtupdate@gmail.com".equals(moderatorToUpdate.getEmail()));
//
//        moderatorToUpdate.setEmail("newmoderator@gmail.com");
//        userRepository.update(session, moderatorToUpdate.getUserId(), moderatorToUpdate);
//
//        assertEquals("newmoderator@gmail.com", moderatorToUpdate.getEmail());
//    }
//
//    @Test
//    @DisplayName("testReadUsersByFindLogin- Expected one result per query")
//    public void testReadUsersByFindLogin() {
//        Optional<User> clientHolder = userRepository.findByLogin(clientToUpdate.getLogin());
//        Optional<User> adminHolder = userRepository.findByLogin(adminToUpdate.getLogin());
//        Optional<User> moderatorHolder = userRepository.findByLogin(moderatorToUpdate.getLogin());
//
//        Client clientFindByLogin = new Client(new ObjectId(), "client123fbl", "Client", "Test", "ctfbl@gmail.com", true, Role.CLIENT);
//        Moderator moderatorFindByLogin = new Moderator(new ObjectId(), "moderator123fbl", "Moderator", "Test", "mtfbl@gmail.com", true, Role.MODERATOR);
//        Admin adminFindByLogin = new Admin(new ObjectId(), "admin123fbl", "Admin", "Test", "atfbl@gmail.com", true, Role.ADMIN);
//
//
//        assertAll(
//                () -> assertNotNull(clientHolder),
//                () -> assertEquals(1, clientHolder.stream().count()),
//                () -> assertEquals(1, moderatorHolder.stream().count()),
//                () -> assertEquals(1, adminHolder.stream().count())
//        );
//
//        userRepository.add(session, clientFindByLogin);
//        userRepository.add(session, moderatorFindByLogin);
//        userRepository.add(session, adminFindByLogin);
//
//        assertAll(
//                () -> assertNotNull(clientHolder),
//                () -> assertNotNull(moderatorHolder),
//                () -> assertNotNull(adminHolder),
//                () -> assertEquals(1, clientHolder.stream().count()),
//                () -> assertEquals(1, moderatorHolder.stream().count()),
//                () -> assertEquals(1, adminHolder.stream().count())
//        );
//    }
//
//    @Test
//    public void testReadUsersBySearchLogin() {
//        Client clientSearchByLogin = new Client(new ObjectId(), "client123sbl", "Client", "Test", "ctsbl@gmail.com", true, Role.CLIENT);
//        Moderator moderatorSearchByLogin = new Moderator(new ObjectId(), "moderator123sbl", "Moderator", "Test", "mtsbl@gmail.com", true, Role.MODERATOR);
//        Admin adminSearchByLogin = new Admin(new ObjectId(), "admin123sbl", "Admin", "Test", "atsbl@gmail.com", true, Role.ADMIN);
//
//        userRepository.add(session, clientSearchByLogin);
//        userRepository.add(session, moderatorSearchByLogin);
//        userRepository.add(session, adminSearchByLogin);
//
//        List<User> clientHolder = userRepository.searchByLogin("client123");
//        List<User> adminHolder = userRepository.searchByLogin("admin123");
//        List<User> moderatorHolder = userRepository.searchByLogin("moderator123");
//
//        List<String> clientLogins = clientHolder.stream().map(User::getLogin).toList();
//        List<String> adminLogins = adminHolder.stream().map(User::getLogin).toList();
//        List<String> moderatorLogins = moderatorHolder.stream().map(User::getLogin).toList();
//
//        assertAll(
//                () -> assertNotNull(clientHolder),
//                () -> assertNotNull(moderatorHolder),
//                () -> assertNotNull(adminHolder),
//                () -> assertTrue(clientLogins.contains("client123sbl")),
//                () -> assertTrue(clientLogins.contains("client123")),
//                () -> assertTrue(moderatorLogins.contains("moderator123sbl")),
//                () -> assertTrue(moderatorLogins.contains("moderator123")),
//                () -> assertTrue(adminLogins.contains("admin123sbl")),
//                () -> assertTrue(adminLogins.contains("admin123"))
//        );
//    }
}
