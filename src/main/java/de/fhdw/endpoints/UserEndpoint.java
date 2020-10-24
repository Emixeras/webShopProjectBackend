package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import org.jboss.logging.Logger;
import org.wildfly.common.annotation.NotNull;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    private static final Logger LOG = Logger.getLogger(UserEndpoint.class);

    @GET
    @Path("list")
    @RolesAllowed("admin")
    public List<ShopUser> returnAllUser() {
        LOG.debug("Liste Aller Benutzer abgefragt");
        return ShopUser.listAll();
    }

    @GET
    @PermitAll
    @Transactional
    @Path("init")
    public List<ShopUser> initTestUser() {
        if (ShopUser.findById(1L) == null && ShopUser.findById(2L) == null) {
            ShopUser admin = new ShopUser();
            admin.username = "admin";
            admin.password = "Test1234";
            admin.role = ShopUser.Role.admin;
            LOG.debug("Benutzer angelegt: "+admin.toString() );
            admin.persist();
            ShopUser shopUser = new ShopUser();
            shopUser.username = "user";
            shopUser.password = "Test1234";
            shopUser.birth = new Date(873560374);
            shopUser.role = ShopUser.Role.user;
            shopUser.persist();
            LOG.debug("Benutzer angelegt: "+shopUser.toString() );
        }
        return ShopUser.listAll();
    }

    @GET
    @RolesAllowed("user, admin")
    @Path("login")
    public ShopUser login(@Context SecurityContext securityContext) {
        return ShopUser.findByName(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Transactional
    @PermitAll
    @Path("register")
    public ShopUser register(@NotNull ShopUser shopUser) throws Exception {
        if(ShopUser.findByName(shopUser.username) == null){
            shopUser.persist();
            return shopUser;
        }
        else throw new Exception("Benutzer bereits vorhanden");
    }

}
