package de.fhdw.endpoints;

import de.fhdw.TestHelper;
import de.fhdw.models.*;
import de.fhdw.util.SysInit;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
class UserEndpointTest {

    private static final Logger LOG = Logger.getLogger(UserEndpoint.class);

    @BeforeAll
    @Transactional
    static void beforeAll() {
        SysInit sysInit = new SysInit();
        TestHelper testHelper = new TestHelper();
        testHelper.emptyUserTable();
        assertEquals(0, ShopUser.count());
        assertEquals(0, ShopSys.count());
        assertEquals(0, Genre.count());
        assertEquals(0, GenrePicture.count());
        assertEquals(0, Artist.count());
        assertEquals(0, ArtistPicture.count());
        assertEquals(0, Article.count());
        assertEquals(0, ArticlePicture.count());

        sysInit.initUser();
        Assertions.assertEquals(30, ShopUser.count());
    }

    @AfterAll
    @Transactional
    static void afterAll() {
        TestHelper testHelper = new TestHelper();
        testHelper.emptyDatabase();

        assertEquals(0, ShopUser.count());
        assertEquals(0, ShopSys.count());
        assertEquals(0, Genre.count());
        assertEquals(0, GenrePicture.count());
        assertEquals(0, Artist.count());
        assertEquals(0, ArtistPicture.count());
        assertEquals(0, Article.count());
        assertEquals(0, ArticlePicture.count());
    }


    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    void getAllUserunauthenticated() {

        given()
                .when().get()
                .then()
                .statusCode(401);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    @TestSecurity(user = "admin@admin.de", roles = {"admin"})
    void getAllUserAuthenticatedAsAdmin() {
        given()
                .when().get("/all")
                .then()
                .statusCode(200);

    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    @TestSecurity(user = "employee", roles = {"employee"})
    void getAllUserAuthenticatedAsEmployee() {

        given()
                .when().get("/all")
                .then()
                .statusCode(403);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    @TestSecurity(user = "user@user.de", roles = {"user"})
    void getAllUserAuthenticatedAsUser() {

        given()

                .when().get("/all")
                .then()
                .statusCode(403);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    @TestSecurity(user = "admin@admin.de", roles = {"admin"})
    void getCurrentUser() {

        given()
                .auth()
                .basic("admin@admin.de", "Test1234")
                .when().get()
                .then()
                .statusCode(200);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    void registerDuplicatedUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("firstName", "Test12123");
        jsonAsMap.put("lastName", "Test127123");
        jsonAsMap.put("password", "blub");
        jsonAsMap.put("title", "FRAU");
        jsonAsMap.put("email", "employee@employee.de");
        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");
        LOG.info(jsonAsMap.toString());

        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .post()
                .then()
                .statusCode(409);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    void registerNewUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("firstName", "Test12123");
        jsonAsMap.put("lastName", "Test127123");
        jsonAsMap.put("password", "blub");
        jsonAsMap.put("title", "FRAU");
        jsonAsMap.put("email", "employee123@employee.de");
        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");
        LOG.info(jsonAsMap.toString());
        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .post()
                .then()
                .statusCode(200);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    void registerUserWithNoEMail() {


        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("firstName", "Test127123");
        jsonAsMap.put("lastName", "Test127123");
        jsonAsMap.put("password", "blub");
        jsonAsMap.put("title", "FRAU");
        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");
        LOG.info(jsonAsMap.toString());
        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    void registerIncorrectUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("firstName", "Test127123");
        jsonAsMap.put("lastName", "Test127123");
        jsonAsMap.put("email", "employee123@employee.de");
        jsonAsMap.put("title", "FRAU");
        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");
        LOG.info(jsonAsMap.toString());
        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    @TestHTTPEndpoint(UserEndpoint.class)
    void updateUnauthenticated() {
        given()
                .contentType("application/json")
                .when()
                .put()
                .then()
                .statusCode(401);
    }
}