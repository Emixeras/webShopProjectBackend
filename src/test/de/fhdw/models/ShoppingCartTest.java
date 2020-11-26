package de.fhdw.models;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import java.util.ArrayList;
import java.util.List;

class ShoppingCartTest {


    @Test
    void createShoppingCart() {

        Article article = new Article();
        article.id = 5L;
        article.description = "test";
        article.price = 55;
        article.title = "test";

        ShopUser shopUser = new ShopUser();
        shopUser.role = ShopUser.Role.ADMIN;

        List<CartEntry> cartEntries = new ArrayList<>();
        cartEntries.add(new CartEntry(article, 5));

        ShoppingCart shoppingCart = new ShoppingCart(cartEntries, shopUser);
        Assert.assertNotNull(shoppingCart);
    }



}