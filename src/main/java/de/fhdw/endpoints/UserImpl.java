package de.fhdw.endpoints;

import de.fhdw.models.Address;
import de.fhdw.models.ShopUser;
import org.jboss.logging.Logger;
import org.wildfly.common.annotation.NotNull;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.*;

@ApplicationScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserImpl implements UserInterface {
    private static final Logger LOG = Logger.getLogger(UserImpl.class);

    @GET
    @Path("getAll")
    @RolesAllowed("admin")
    @Override
    public List<ShopUser> getAll() {
        LOG.info("Liste Aller Benutzer abgefragt");
        return ShopUser.listAll();
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Override
    public ShopUser get(@Context SecurityContext securityContext) {
        return ShopUser.findByName(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Transactional
    @PermitAll
    @Override
    public ShopUser post(@NotNull ShopUser shopUser) throws Exception {
        if (ShopUser.findByName(shopUser.username) == null) {
            shopUser.role = ShopUser.Role.USER;
            if(shopUser.addresses != null){
             List<Address> addresses = new ArrayList<>();
                shopUser.addresses.stream().forEach(i -> {
                 Address temp = new Address();
                 temp.street = i.street;
                 temp.postalCode = i.postalCode;
                 temp.country = i.country;
                 addresses.add(temp);
             });
                shopUser.addresses = addresses;
            }
            shopUser.persist();
            return shopUser;
        } else throw new Exception("Benutzer bereits vorhanden");
    }

    @PUT
    @Transactional
    @RolesAllowed({"user", "admin"})
    @Override
    public ShopUser put(@NotNull ShopUser shopUser, @Context SecurityContext securityContext) throws Exception {
        ShopUser user = ShopUser.findByName(securityContext.getUserPrincipal().getName());
        if (user.role == ShopUser.Role.ADMIN || user.id.equals(shopUser.id)) {
            user.email = shopUser.email;
            user.lastName = shopUser.lastName;
            user.firstName = shopUser.firstName;
            if(shopUser.addresses != null){
                user.addresses.clear();
                user.addresses.addAll(shopUser.addresses);
            }
            user.password = shopUser.password;
            user.birth = shopUser.birth;
            return user;
        } else throw new Exception("Permission violation");
    }

    @Override
    @Path("{username}")
    @DELETE
    @Transactional
    public Boolean delete(@PathParam String username, @Context SecurityContext securityContext) {
        ShopUser deleted = ShopUser.findByName(username);
        ShopUser shopUser = ShopUser.findByName(securityContext.getUserPrincipal().getName());
        if (shopUser.role == ShopUser.Role.ADMIN || username.equals(deleted.username)) {
            deleted.delete();
            return true;
        }
        return false;
    }


}
