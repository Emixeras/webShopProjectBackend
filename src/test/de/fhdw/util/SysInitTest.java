package de.fhdw.util;

import de.fhdw.endpoints.UserImpl;
import de.fhdw.models.*;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class SysInitTest {
    private static final Logger LOG = Logger.getLogger(UserImpl.class);


    @Test
    @TestTransaction
    void CheckSysTable() {
        SysInit sysInit = new SysInit();

        sysInit.demoData = false;
        sysInit.onStart(new StartupEvent());
        assertEquals(0, ShopSys.count());


        sysInit.demoData = true;
        sysInit.onStart(new StartupEvent());
        int user = 30;
        assertEquals(user, ShopUser.count());
        assertEquals(4, ShopSys.count());
        int genre = 16;
        assertEquals(genre, Genre.count());
        int artist = 50;
        assertEquals(artist, Artist.count());
        int article = 500;
        assertEquals(article, Article.count());
    }

}