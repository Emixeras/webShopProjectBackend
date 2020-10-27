package de.fhdw.endpoints;

import de.fhdw.models.HelloWorld;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TestImplTest {

    @Test
    void userTestData() {
    }

    @Test
    void get() {
        given()
                .when().get("/api/helloWorld/get")
                .then()
                .statusCode(200)
                .body("value", is("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @Test
    void post() {
        HelloWorld helloWorld = new HelloWorld("hello");
        given()
                .contentType(ContentType.JSON)
                .body(helloWorld)
                .post("/api/helloWorld/post")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("value", equalTo(helloWorld.value));
    }

    @Test
    void getAuthenticated() {
    }
}