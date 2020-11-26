package de.fhdw.models;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

@QuarkusTest
class ShopShopOrderEntryTest {

    @Test
    void createOrderEntry(){
        Article article = new Article();
        ShopOrderEntry shopOrderEntry = new ShopOrderEntry(new ShopOrderArticle(article), 5);
        Assert.assertNotNull(shopOrderEntry);
    }


}