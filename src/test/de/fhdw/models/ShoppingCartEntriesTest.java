package de.fhdw.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

@QuarkusTest
class ShoppingCartEntriesTest {

    @Test
    void createShopOrderCart(){

        Article article = new Article();
        article.title="test";
        ShoppingCartEntries shoppingCartEntries = new ShoppingCartEntries(article, 5);
        shoppingCartEntries.count = 50;

        Assert.assertNotNull(shoppingCartEntries);

    }

}