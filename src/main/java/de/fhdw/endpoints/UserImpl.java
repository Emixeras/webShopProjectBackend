package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import de.fhdw.util.PermissionUtil;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.wildfly.common.annotation.NotNull;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@ApplicationScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User", description = "Operations on User Object")
public class UserImpl implements UserInterface {
    private static final Logger LOG = Logger.getLogger(UserImpl.class);

    @GET
    @Path("all")
    @RolesAllowed("admin")
    @Override
    @Operation(summary = "Returns all User as Json List")
    public List<ShopUser> getAllUser() {
        LOG.info("Liste Aller Benutzer abgefragt");
        return ShopUser.listAll();
    }

    @GET
    @RolesAllowed({"admin", "user", "employee"})
    @Override
    @Operation(summary = "Returns a single User identified by id")
    public ShopUser getCurrentUser(@Context SecurityContext securityContext) {
        return ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Transactional
    @PermitAll
    @Override
    @Operation(summary = "registers a new User in Database")
    public ShopUser registerNewUser(@NotNull ShopUser shopUser) {
        if (ShopUser.findbyEmail(shopUser.email) == null) {
            shopUser.role = ShopUser.Role.USER;
            if (shopUser.firstName.equals("") || shopUser.lastName.equals("")) {
                throw new WebApplicationException(Response.Status.NO_CONTENT);
            }
            shopUser.persist();
            LOG.info("added: " + shopUser.toString());
            return shopUser;
        } else throw new WebApplicationException(Response.Status.valueOf("Benutzername bereits vorhanden"));
    }

    @PUT
    @Transactional
    @RolesAllowed({"user", "admin", "employee"})
    @Override
    @Operation(summary = "modifies a User")
    public ShopUser updateUser(@NotNull ShopUser newUserInformation, @Context SecurityContext securityContext) {
        ShopUser requestingUser = ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());
        ShopUser changedUser;
        LOG.info(newUserInformation.email);

        changedUser = ShopUser.findById(newUserInformation.id);
        if (changedUser == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        PermissionUtil permissionUtil = new PermissionUtil(requestingUser, newUserInformation);
        if (!changedUser.equals(newUserInformation)) {
            if (permissionUtil.checkIfAdmin() || permissionUtil.checkIfUserIsSelf()) {
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
                LOG.info("new User: " + changedUser.toString());
            } else throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

        if (permissionUtil.checkIfRolesAreTheSame() && permissionUtil.checkIfAdminOrEmployee()) {
            if (permissionUtil.checkIfNewRoleIsEmployee()) {
                LOG.debug("Benutzer zu Employee promotet");
                changedUser.role = newUserInformation.role;
                return changedUser;
            } else if (permissionUtil.checkIfNewRoleIsAdminAndRightsAreSufficient()) {
                changedUser.role = ShopUser.Role.ADMIN;
                LOG.debug("Benutzer zu Admin promotet");
                return changedUser;
            } else {
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
    @Operation(summary = "deletes a user identified by id if permissions are correct")
    public Boolean deleteUser(@PathParam String email, @Context SecurityContext securityContext) {
        ShopUser deleted = ShopUser.findbyEmail(email);
        ShopUser shopUser = ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());
        if (deleted == null || shopUser == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (shopUser.role == ShopUser.Role.ADMIN || email.equals(deleted.email)) {
            try {
                deleted.delete();
            } catch (Exception e) {
                throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
            }
            return true;
        }
        return false;
    }


}
