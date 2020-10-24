package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.json.bind.Jsonb;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserEndpointTest {

    @Test
    void returnAllUser() {
        PanacheMock.mock(ShopUser.class);
        Assertions.assertEquals(0, ShopUser.count());
        Mockito.when(ShopUser.count()).thenReturn(100l);
        Assertions.assertEquals(100, ShopUser.count());
    }



    @Test
    void login() {
        PanacheMock.mock(ShopUser.class);


    }

    @Test
    void register() {
        PanacheMock.mock(ShopUser.class);
    }

    @Test
    void editUser(){
        PanacheMock.mock(ShopUser.class);


    }
}