package de.fhdw.endpoints;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;

class HelloWorldEndpointTest {

    @Test
    void sendHelloWorld() {



    }

    @Test
    void getHelloWorld() {
        given()
                .when().get("/api/HelloWorld/get")
                .then()
                .statusCode(200)
                .body("value", is("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }
}