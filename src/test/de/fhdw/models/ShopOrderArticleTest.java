package de.fhdw.models;

import de.fhdw.TestHelper;
import de.fhdw.util.SysInit;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ShopOrderArticleTest {


    @BeforeAll
    @Transactional
    static void beforeAll() {
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
    @TestTransaction
    void create() {
        Article article = Article.findById(1L);
        ShopOrderArticle shopOrderArticle = new ShopOrderArticle(article);
        shopOrderArticle.persist();
    }

    @Test
    @TestTransaction
    void checkForChanges(){
        Article article = Article.findById(1L);
    }

}
