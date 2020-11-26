package de.fhdw.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

@QuarkusTest
class ShopOrderCartTest {

    @Test
    void createShopOrderCart(){

        Article article = new Article();
        article.title="test";

        ShopOrderCart shopOrderCart = new ShopOrderCart(article, 5);
        shopOrderCart.article = article;
        shopOrderCart.quantity = 50;

        Assert.assertNotNull(shopOrderCart);


    }

}