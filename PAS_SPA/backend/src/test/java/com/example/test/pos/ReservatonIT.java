package com.example.test.pos;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReservatonIT {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/PAS_PD-1/api/v1";
    }

    private String createClientAndGetId() {
        String login = "client_" + UUID.randomUUID().toString().substring(0, 8);
        String email = login + "@example.com";

        String body = """
                {
                  "login": "%s",
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "%s",
                  "isActive": true
                }
                """.formatted(login, email);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users/client")
                .then()
                .statusCode(200);

        return given()
                .queryParam("login", login)
                .when()
                .get("/users/search")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .extract()
                .path("[0].id");
    }

    private String createRoomAndGetId() {
        String body = """
                {
                  "roomType": "COURT",
                  "capacity": 10,
                  "basePrice": 10.0
                }
                """;

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/rooms")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract()
                .path("id");
    }


    @Test
    void testCreateReservationForClient() {
        String clientId = createClientAndGetId();
        String roomId = createRoomAndGetId();

        String reservationBody = """
                {
                  "clientId": "%s",
                  "roomId": "%s",
                  "startTime": "2025-11-20T10:00:00",
                  "price": 10.0
                }
                """.formatted(clientId, roomId);

        given()
                .contentType(ContentType.JSON)
                .body(reservationBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(200);

        given()
                .queryParam("status", "current")
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void testCreateSameReservationOfRoomForClient() {
        String clientId1 = createClientAndGetId();
        String clientId2 = createClientAndGetId();
        String roomId = createRoomAndGetId();

        String reservationBody1 = """
                {
                  "clientId": "%s",
                  "roomId": "%s",
                  "startTime": "2025-11-20T10:00:00",
                  "price": 10.0
                }
                """.formatted(clientId1, roomId);
        String reservationBody2 = """
                {
                  "clientId": "%s",
                  "roomId": "%s",
                  "startTime": "2025-11-20T10:00:00",
                  "price": 10.0
                }
                """.formatted(clientId2, roomId);

        given()
                .contentType(ContentType.JSON)
                .body(reservationBody1)
                .when()
                .post("/reservations")
                .then()
                .statusCode(200);
        given()
                .contentType(ContentType.JSON)
                .body(reservationBody2)
                .when()
                .post("/reservations")
                .then()
                .statusCode(500);
    }

}
