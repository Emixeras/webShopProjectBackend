package de.fhdw.endpoints;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import de.fhdw.models.HelloWorld;
import de.fhdw.models.ShopUser;

public interface TestInterface {
    public  List<ShopUser> userTestData();
    public HelloWorld get();
    public HelloWorld post(HelloWorld helloWorld);
    public String getAuthenticated(@Context SecurityContext securityContext);

    }
