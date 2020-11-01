package de.fhdw.endpoints;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import de.fhdw.models.Article;
import de.fhdw.models.HelloWorld;
import de.fhdw.models.ShopUser;

public interface TestInterface {
    public  List<ShopUser> userTestData();
    public List<Article> articleTestData();
    public HelloWorld get();
    public HelloWorld post(HelloWorld helloWorld);
    public String getAdmin(@Context SecurityContext securityContext);
    public String getUser(@Context SecurityContext securityContext);
    public String getEmployee(@Context SecurityContext securityContext);
    }
