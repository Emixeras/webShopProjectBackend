package de.fhdw.util;

import de.fhdw.TestHelper;
import de.fhdw.endpoints.UserEndpoint;
import de.fhdw.models.*;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
class SysInitTest {
    private static final Logger LOG = Logger.getLogger(UserEndpoint.class);

    @BeforeAll
    @Transactional
    static void  emptyDatabase(){
        TestHelper testHelper = new TestHelper();
        testHelper.emptyDatabase();
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
    void CheckSysTable() {
        assertEquals(0, ShopUser.count());
        assertEquals(0, ShopSys.count());
        assertEquals(0, Genre.count());
        assertEquals(0, GenrePicture.count());
        assertEquals(0, Artist.count());
        assertEquals(0, ArtistPicture.count());
        assertEquals(0, Article.count());
        assertEquals(0, ArticlePicture.count());

        SysInit sysInit = new SysInit();

        sysInit.demoData = false;
        sysInit.onStart(new StartupEvent());
        assertEquals(0, ShopSys.count());


        sysInit.demoData = true;
        sysInit.onStart(new StartupEvent());
        int user = 30;
        assertEquals(user, ShopUser.count());
        int shopSys= 4;
        assertEquals(shopSys, ShopSys.count());
        int genre = 16;
        assertEquals(genre, Genre.count());
        int artist = 50;
        assertEquals(artist, Artist.count());
        int article = 500;
        assertEquals(article, Article.count());

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

}