package de.fhdw.models;

import de.fhdw.TestHelper;
import de.fhdw.util.SysInit;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ShopOrderTest {

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

    SecurityContext securityContext = new SecurityContext() {
        @Override
        public Principal getUserPrincipal() {
            return new UserPrincipal() {
                @Override
                public String getName() {
                    return "user@user.de";
                }
            };
        }

        @Override
        public boolean isUserInRole(String s) {
            return false;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            return null;
        }
    };



    @Test
    @TestTransaction
    void createOrder() {
        Article article = Article.findById(1L);

        Assert.assertNotNull(article);
        ShopUser shopUser = new ShopUser();
        shopUser.role = ShopUser.Role.ADMIN;

        List<ShopOrderEntry> cartEntries = new ArrayList<>();
        cartEntries.add(new ShopOrderEntry(new ShopOrderArticle(article), 5));

        ShopOrder shopOrder = new ShopOrder(cartEntries, shopUser);
        Assert.assertNotNull(shopOrder);
    }



}