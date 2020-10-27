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
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserImpl implements UserInterface {
    private static final Logger LOG = Logger.getLogger(UserImpl.class);

    @GET
    @Path("list")
    @RolesAllowed("admin")
    @Override
    public List<ShopUser> getAll() {
        LOG.info("Liste Aller Benutzer abgefragt");
        return ShopUser.listAll();
    }

  

    @GET
    @RolesAllowed({"admin", "user"})
    @Path("login")
    @Override
    public ShopUser get(@Context SecurityContext securityContext) {
        return ShopUser.findByName(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Transactional
    @PermitAll
    @Path("register")
    @Override
    public ShopUser post(@NotNull ShopUser shopUser) throws Exception {
        if(ShopUser.findByName(shopUser.username) == null){
            shopUser.persist();
            return shopUser;
        }
        else throw new Exception("Benutzer bereits vorhanden");
    }

    @POST
    @Transactional
    @RolesAllowed("user, admin")
    @Path("{username}")
    @Override
    public ShopUser put(@PathParam String username, @NotNull ShopUser shopUser, @Context SecurityContext securityContext) throws Exception {
       ShopUser user =  ShopUser.findByName(securityContext.getUserPrincipal().getName());
       if(user.role == ShopUser.Role.ADMIN || user.id.equals(shopUser.id)){
           user = shopUser;
           user.persist();
           return user;
       }
       else throw new Exception("Permission violation");
    }

    @Override
    public Boolean delete(@PathParam String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ShopUser put(ShopUser shopUser, SecurityContext securityContext) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }


   

}
