package de.fhdw.endpoints;

import de.fhdw.models.Address;
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
import java.util.ArrayList;
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
        LOG.info("Liste Aller Benutzer abgefragt");
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
            admin.addresses = new ArrayList<Address>();

            admin.role = ShopUser.Role.admin;
            LOG.info("Benutzer angelegt: "+admin.toString() );
            admin.persist();
            ShopUser shopUser = new ShopUser();
            shopUser.username = "user";
            shopUser.password = "Test1234";
            shopUser.firstName = "Christoph";
            shopUser.lastName = "Müller";
            shopUser.addresses = new ArrayList<Address>();
            shopUser.birth = new Date(873560374);
            shopUser.role = ShopUser.Role.user;
            shopUser.persist();
            LOG.info("Benutzer angelegt: "+shopUser.toString() );
        }
        return ShopUser.listAll();
    }

    @GET
    @RolesAllowed({"admin", "user"})
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

    @POST
    @Transactional
    @RolesAllowed("user, admin")
    @Path("edit")
    public ShopUser editUser(@NotNull ShopUser shopUser, @Context SecurityContext securityContext) throws Exception {
       ShopUser user =  ShopUser.findByName(securityContext.getUserPrincipal().getName());
       if(user.role.equals("admin") || user.id.equals(shopUser.id)){
           user = shopUser;
           user.persist();
           return user;
       }
       else throw new Exception("Permission violation");
    }

}
