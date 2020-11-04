package de.fhdw.endpoints;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.*;

@ApplicationScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserImpl implements UserInterface  {
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
    @RolesAllowed({"admin", "user", "employee"})
    @Override
    public ShopUser get(@Context SecurityContext securityContext) {
        return ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Transactional
    @PermitAll
    @Override
    public ShopUser post(@NotNull ShopUser shopUser) {
        if (ShopUser.findbyEmail(shopUser.email) == null) {
            shopUser.role = ShopUser.Role.USER;
            if(shopUser.firstName.equals("") || shopUser.lastName.equals("")){
                throw new WebApplicationException(Response.Status.NO_CONTENT);
            }
            shopUser.persist();
            LOG.info("added: "+shopUser.toString());
            return shopUser;
        } else throw new WebApplicationException(Response.Status.valueOf("Benutzername bereits vorhanden"));
    }

    @PUT
    @Transactional
    @RolesAllowed({"user", "admin", "employee"})
    @Override
    public ShopUser put(@NotNull ShopUser newUserInformation, @Context SecurityContext securityContext) {
        ShopUser requestingUser = ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());
        ShopUser changedUser;
        LOG.info(newUserInformation.email);

            changedUser = ShopUser.findById(newUserInformation.id);
         if(changedUser == null){
             throw new WebApplicationException(Response.Status.NOT_FOUND);
         }

            LOG.info(changedUser.email);
        //update Informations
        if (requestingUser.role == ShopUser.Role.ADMIN || requestingUser.id.equals(newUserInformation.id)) {
          //changedUser.getChangesFrom(newUserInformation); //todo: check why reflections donÄt work
            changedUser.email = newUserInformation.email;
            changedUser.lastName = newUserInformation.lastName;
            changedUser.firstName = newUserInformation.firstName;
            changedUser.postalCode = newUserInformation.postalCode;
            changedUser.streetNumber = newUserInformation.streetNumber;
            changedUser.street = newUserInformation.street;
            changedUser.town = newUserInformation.town;
            changedUser.title = newUserInformation.title;
            changedUser.password = newUserInformation.password;
            changedUser.birth = newUserInformation.birth;
            LOG.info("new User: "+changedUser.toString());
        } else throw new WebApplicationException(Response.Status.FORBIDDEN);
        //Prüfe ob Rollen im neuen Objekt differieren und ob Benutzer Admin oder Employee ist
        if ((newUserInformation.role != changedUser.role) && (requestingUser.role == ShopUser.Role.ADMIN || requestingUser.role == ShopUser.Role.EMPLOYEE)) {
            //Wenn neue Benutzerrolle Employee dann setze Sie einfach
            if ((newUserInformation.role == ShopUser.Role.EMPLOYEE) || newUserInformation.role == ShopUser.Role.USER) {
                LOG.debug("Benutzer zu Employee promotet");
                changedUser.role = newUserInformation.role;
                return changedUser;
            }
            //Wenn Benutzerrolle Admin ist, dann prüfe das erneut und setze es dann erst
            else if (newUserInformation.role == ShopUser.Role.ADMIN && requestingUser.role == ShopUser.Role.ADMIN) {
                changedUser.role = ShopUser.Role.ADMIN;
                LOG.debug("Benutzer zu Admin promotet");
                return changedUser;
            }
            else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        }
        return changedUser;
    }

    @Override
    @Path("{email}")
    @DELETE
    @RolesAllowed({"user", "admin", "employee"})
    @Transactional
    public Boolean delete(@PathParam String email, @Context SecurityContext securityContext) {
        ShopUser deleted = ShopUser.findbyEmail(email);
        ShopUser shopUser = ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());
        if(deleted == null || shopUser == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (shopUser.role == ShopUser.Role.ADMIN || email.equals(deleted.email)) {
            try {

                deleted.delete();
            }catch (Exception e){
                throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
            }
            return true;
        }
        return false;
    }


}
