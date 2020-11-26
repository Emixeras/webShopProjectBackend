package de.fhdw.models;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CartEntryTest {

    @Test
    void createEntry(){
        Article article = new Article();
        CartEntry cartEntry = new CartEntry(article, 5);
        Assert.assertNotNull(cartEntry);
    }


}