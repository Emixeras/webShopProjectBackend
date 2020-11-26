package de.fhdw.models;

import de.fhdw.TestHelper;
import de.fhdw.util.SysInit;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    void createOrder() {
        Article article = Article.findById(1L);

        ShopUser shopUser = new ShopUser();
        shopUser.role = ShopUser.Role.ADMIN;

        List<ShopOrderEntry> cartEntries = new ArrayList<>();
        cartEntries.add(new ShopOrderEntry(new ShopOrderArticle(article), 5));

        ShopOrder shopOrder = new ShopOrder(cartEntries, shopUser);
        Assert.assertNotNull(shopOrder);
    }



}