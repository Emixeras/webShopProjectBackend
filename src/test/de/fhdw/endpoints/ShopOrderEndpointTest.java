package de.fhdw.endpoints;

import de.fhdw.TestHelper;
import de.fhdw.models.*;
import de.fhdw.util.SysInit;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@QuarkusTest
public class ShopOrderEndpointTest {
    private static final Logger LOG = Logger.getLogger(ShopOrderEndpointTest.class);
    ShopUser shopUser;
    OrderEndpoint orderEndpoint = new OrderEndpoint();
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
        // testHelper.emptyDatabase();
    }


    @Test
    @Transactional
    void createOrder() {
        shopUser = ShopUser.findByEmail("user@user.de");
        List<ShoppingCartEntries> cartEntries = new ArrayList<>();
        ShoppingCart shoppingCart = new ShoppingCart();
        IntStream.range(0, 10).forEach(i -> {
            IntStream.range(0, 2).forEach(n -> {
                ShoppingCartEntries shoppingCartEntries = new ShoppingCartEntries(Article.findById((Integer.toUnsignedLong(new Random().nextInt(49) + 1))), (new Random().nextInt(9)) + 1);
                cartEntries.add(shoppingCartEntries);
            });
            shoppingCart.shoppingCartEntries = cartEntries;
            shoppingCart.paymentMethod = ShopOrder.paymentMethod.VORKASSE;
            shoppingCart.shipping = 15.99;
            orderEndpoint.createOrder(shoppingCart, securityContext);
        });


    }

    @Test
    void getOrderForUser() {
        shopUser = ShopUser.findByEmail("user@user.de");
        Optional<ShopUser> optionalShopUser = Optional.ofNullable(shopUser);
        Response order = orderEndpoint.getOrderForUser(securityContext);
        Assert.assertNotNull(order);

    }


}
