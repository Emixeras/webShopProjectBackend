package de.fhdw.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import java.util.Date;

@QuarkusTest
class ShopUserTest {


    @Test
    void checkIfUserCorrect(){
        ShopUser shopUser = new ShopUser();
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.email="test@test.de";
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.firstName = "test";
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.lastName = "test";
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.role = ShopUser.Role.ADMIN;
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.birth = new Date();
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.title = ShopUser.Title.APACHE;
        Assert.assertFalse(shopUser.checkIfUserIsCorrect());

        shopUser.password = "ShopUser.Title.APACHE;";
        Assert.assertTrue(shopUser.checkIfUserIsCorrect());


    }

}