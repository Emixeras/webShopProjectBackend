package de.fhdw.endpoints;

import de.fhdw.models.Article;
import de.fhdw.models.HelloWorld;
import de.fhdw.models.ShopUser;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface TestInterface {
    List<ShopUser> userTestData();

    List<Article> articleTestData();

    HelloWorld get();

    HelloWorld post(HelloWorld helloWorld);

    String getAdmin(@Context SecurityContext securityContext);

    String getUser(@Context SecurityContext securityContext);

    String getEmployee(@Context SecurityContext securityContext);
}
