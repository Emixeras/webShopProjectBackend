package de.fhdw.endpoints;

import de.fhdw.models.ShopOrderItem;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class OrderImplTest {

    @Test
    @TestHTTPEndpoint(OrderImpl.class)
    @TestSecurity(user = "admin@admin.de", roles = {"admin"})
    void testGetAllOrdersForUser() {

        given()
                .contentType("application/json")
                .body(stringObjectMap)
                .when()
                .post()
                .then()
                .statusCode(200);

    }

    @Test
    void testOrderNewArticle() {
    }
}