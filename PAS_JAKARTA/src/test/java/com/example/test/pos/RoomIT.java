package com.example.test.pos;

import com.example.controller.RoomController;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RoomIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/pas";
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
                .post(RoomController.BASE_URL)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)))
                .body("roomType", equalTo("COURT"))
                .body("capacity", equalTo(10))
                .body("basePrice", equalTo(10.0f))
                .extract()
                .path("id");
    }

    @Test
    void testCreateRoom() {
        String body = """
            {
              "roomType": "COURT",
              "capacity": 10,
              "basePrice": 10.0
            }
            """;

        given().contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(RoomController.BASE_URL)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)))
                .body("id", notNullValue())
                .body("roomType", equalTo("COURT"))
                .body("capacity", equalTo(10))
                .body("basePrice", equalTo(10.0f));
    }


    @Test
    void testGetRoomById() {

        String id = createRoomAndGetId();

        given().contentType(ContentType.JSON)
                .when()
                .get(RoomController.BASE_ID_URL, id)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)))
                .body("id", notNullValue())
                .body("roomType", equalTo("COURT"))
                .body("capacity", equalTo(10))
                .body("basePrice", equalTo(10.0f));

    }



    @Test
    void testUpdateRoom() {
        String id = createRoomAndGetId();

        String updateBody = """
            {
              "roomType": "GYM",
              "capacity": 20,
              "basePrice": 15.5
            }
            """;

        given().contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put(RoomController.BASE_ID_URL, id)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)));

        given().when()
                .get(RoomController.BASE_ID_URL, id)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)))
                .body("id", equalTo(id))
                .body("roomType", equalTo("GYM"))
                .body("capacity", equalTo(20))
                .body("basePrice", equalTo(15.5f));
    }

    @Test
    void testDeleteRoom() {
        String id = createRoomAndGetId();

        given().when()
                .delete(RoomController.BASE_ID_URL, id)
                .then()
                .statusCode(anyOf(is(200), is(201), is(204)));

        given().when()
                .get(RoomController.BASE_ID_URL, id)
                .then()
                .statusCode(404);
    }




    @Test
    void testRejectInvalidRoom() {
        String jsonBody = """
            {
              "roomType": "COURT",
              "capacity": -1,
              "basePrice": 10.0
            }
            """;

        given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(RoomController.BASE_URL)
                .then()
                .statusCode(400);
    }
}
