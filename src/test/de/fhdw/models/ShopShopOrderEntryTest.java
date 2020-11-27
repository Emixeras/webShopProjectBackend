package de.fhdw.models;

import de.fhdw.TestHelper;
import de.fhdw.util.SysInit;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ShopShopOrderEntryTest {

    @BeforeAll
    @Transactional
    static void setUp() {
        TestHelper testHelper = new TestHelper();
        testHelper.emptyDatabase();
        SysInit sysInit = new SysInit();
        sysInit.lazyDemoData = true;
        sysInit.initDemoData();
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
    void createOrderEntry(){
        Article article = Article.findById(1L);
        Assert.assertNotNull(article);
        ShopOrderEntry shopOrderEntry = new ShopOrderEntry(new ShopOrderArticle(article), 5);
        Assert.assertNotNull(shopOrderEntry);
    }


}