package de.fhdw.endpoints;

import de.fhdw.TestHelper;
import de.fhdw.models.Article;
import de.fhdw.models.ShopOrderEntry;
import de.fhdw.models.ShopUser;
import de.fhdw.util.SysInit;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import javax.ws.rs.core.SecurityContext;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@QuarkusTest
public class ShopOrderEndpointTest {
    ShopUser shopUser;
    OrderEndpoint orderEndpoint = new OrderEndpoint();


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

    @AfterAll
    @Transactional
    static void afterAll() {
        TestHelper testHelper = new TestHelper();
        testHelper.emptyDatabase();
    }




    @Test
    @Transactional
    void createOrder(){
        shopUser = ShopUser.findByEmail("user@user.de");
        List<ShopOrderEntry> cartEntries = new ArrayList<>();
        IntStream.range(0,50).forEach(i->{
            ShopOrderEntry shopOrderEntry = new ShopOrderEntry(Article.findById((Integer.toUnsignedLong(new Random().nextInt(49)+1))), (new Random().nextInt(9))+1);
            cartEntries.add(shopOrderEntry);
        });
        orderEndpoint.createOrder(cartEntries, securityContext);

    }

    @Test
    void getOrderForUser(){
        orderEndpoint.getOrder();
    }


}
