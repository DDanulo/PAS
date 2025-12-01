package com.example.test.pos;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class UserIT {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/PAS_PD-1/api/v1/users";
    }

    private String createClientAndGetId(String login) {
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
                .post("/client")
                .then()
                .statusCode(200);

        return given()
                .queryParam("login", login)
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].login", equalTo(login))
                .extract()
                .path("[0].id");
    }

    @Test
    void testCreateClient() {
        String login = "client_" + UUID.randomUUID().toString().substring(0, 8);
        String email = login + "@example.com";

        String body = """
            {
              "login": "%s",
              "firstName": "Alice",
              "lastName": "Smith",
              "email": "%s",
              "isActive": true
            }
            """.formatted(login, email);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/client")
                .then()
                .statusCode(200);

        given()
                .queryParam("login", login)
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("[0].login", equalTo(login))
                .body("[0].email", equalTo(email))
                .body("[0].role", equalTo("CLIENT"));
    }

    @Test
    void testGetUserById() {
        String login = "client_" + UUID.randomUUID().toString().substring(0, 8);
        String id = createClientAndGetId(login);

        given()
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("login", equalTo(login));
    }

    @Test
    void testUpdateClient() {
        String login = "client_" + UUID.randomUUID().toString().substring(0, 8);
        String id = createClientAndGetId(login);

        String updatedBody = """
            {
              "login": "%s",
              "firstName": "Bob",
              "lastName": "Updated",
              "email": "%s",
              "isActive": true
            }
            """.formatted(login, login + "@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(updatedBody)
                .when()
                .put("/{id}", id)
                .then()
                .statusCode(200); // void → 200

        given()
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("login", equalTo(login))
                .body("firstName", equalTo("Bob"))
                .body("lastName", equalTo("Updated"));
    }

    @Test
    void testActivateAndDeactivateClient() {
        String login = "client_" + UUID.randomUUID().toString().substring(0, 8);
        String id = createClientAndGetId(login);

        given()
                .when()
                .post("/{id}/deactivate", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body("isActive", is(false));

        given()
                .when()
                .post("/{id}/activate", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body("isActive", is(true));
    }

    @Test
    void testListAllUsers() {
        String login = "client_" + UUID.randomUUID().toString().substring(0, 8);
        createClientAndGetId(login);

        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void testBadLoginCreate() {
        String login = "abc";
        String email = login + "@example.com";

        String body = """
            {
              "login": "%s",
              "firstName": "Alice",
              "lastName": "Smith",
              "email": "%s",
              "isActive": true
            }
            """.formatted(login, email);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/client")
                .then()
                .statusCode(400);

    }
    @Test
    void testSameLoginCreate() {
        String login = "test" + UUID.randomUUID().toString().substring(0, 8);;
        String email = login + "@example.com";

        String body = """
            {
              "login": "%s",
              "firstName": "Alice",
              "lastName": "Smith",
              "email": "%s",
              "isActive": true
            }
            """.formatted(login, email);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/client")
                .then()
                .statusCode(200);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/client")
                .then()
                .statusCode(500);

    }
}
