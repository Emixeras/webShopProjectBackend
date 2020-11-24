package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import de.fhdw.util.SysInit;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;



@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class UserImplTest {

    private static final Logger LOG = Logger.getLogger(UserImpl.class);



    @BeforeAll
    @Transactional()
    static void setUp() {

        SysInit sysInit = new SysInit();
        sysInit.initUser();
    }


    @Test
    @TestHTTPEndpoint(UserImpl.class)
    void getAllUserunauthenticated() {

        given()
                .when().get()
                .then()
                .statusCode(401);
    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "admin@admin.de", roles = {"admin"})
    void getAllUserAuthenticatedAsAdmin() {
        given()
                .when().get("/all")
                .then()
                .statusCode(200);

    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "employee", roles = {"employee"})
    void getAllUserAuthenticatedAsEmployee() {

        given()
                .when().get("/all")
                .then()
                .statusCode(403);
    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "user@user.de", roles = {"user"})
    void getAllUserAuthenticatedAsUser() {

        given()

                .when().get("/all")
                .then()
                .statusCode(403);
    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
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
    @TestHTTPEndpoint(UserImpl.class)
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
    @TestHTTPEndpoint(UserImpl.class)
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
    @TestHTTPEndpoint(UserImpl.class)
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
                .statusCode(409);
    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
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
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "admin@admin.de", roles = {"admin"}, authorizationEnabled = true)
    void updateUserAsAdmin() {

    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
    void updateUnauthenticated() {
        given()
                .contentType("application/json")
                .when()
                .put()
                .then()
                .statusCode(401);
    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "user@user.de", roles = {"user"})
    void tryUpdateingDifferentUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("firstName", "Test12123");
        jsonAsMap.put("id", "5");
        jsonAsMap.put("lastName", "Test127123");
        jsonAsMap.put("password", "123123123");
        jsonAsMap.put("title", "FRAU");
        jsonAsMap.put("role","USER");
        jsonAsMap.put("email", "user@user.de");
        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");

        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .put()
                .then()
                .statusCode(400);
    }


    @Test
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "user@user.de", roles = {"user"})
    void updateCorrectUserInformation() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("firstName", "user");
        jsonAsMap.put("id", "2");
        jsonAsMap.put("lastName", "sdf");
        jsonAsMap.put("password", "123123123");
        jsonAsMap.put("title", "FRAU");
        jsonAsMap.put("role","USER");
        jsonAsMap.put("email", "user@user.de");
        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");

        LOG.info(jsonAsMap.toString());
        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .put()
                .then()
                .statusCode(200);
    }

    @Test
    @TestHTTPEndpoint(UserImpl.class)
    @TestSecurity(user = "admin@admin.de", roles = {"admin"})
    void promoteUserAsAdmin() {
        Map<String, Object> jsonAsMap = new HashMap<>();

        jsonAsMap.put("birth", "2020-01-26T09:16:50Z[UTC]");
        jsonAsMap.put("email", "mployee@employee.de");
        jsonAsMap.put("firstName", "Annis");
        jsonAsMap.put("id", "3");
        jsonAsMap.put("lastName","Jeyness");
        jsonAsMap.put("postalCode", "18303");
        jsonAsMap.put("role", "ADMIN");
        jsonAsMap.put("street", "ADMIN");
        LOG.info(jsonAsMap.toString());
        given()
                .contentType("application/json")
                .body(jsonAsMap)
                .when()
                .put("role")
                .then()
                .statusCode(200);
    }


   
}