package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Path("/api/User")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {

    @GET
    @PermitAll
    @Path("/all")
    public List<ShopUser> returnAllUser() {
        return ShopUser.listAll();
    }

    @GET
    @PermitAll
    @Transactional
    @Path("/init")
    public List<ShopUser> initTestUser() {
        if (ShopUser.findById(1L) == null && ShopUser.findById(2L) == null) {
            ShopUser admin = new ShopUser();
            admin.username = "admin";
            admin.password = "Test1234";
            admin.persist();
            ShopUser shopUser = new ShopUser();
            shopUser.username = "user";
            shopUser.password = "Test1234";
            shopUser.birth = new Date(873560374);
            shopUser.persist();
        }
        return ShopUser.listAll();
    }


}
