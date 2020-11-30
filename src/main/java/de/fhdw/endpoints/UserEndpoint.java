package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import de.fhdw.util.PermissionUtil;
import de.fhdw.util.RestError;
import io.quarkus.panache.common.Sort;
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
public class UserEndpoint {
    private static final Logger LOG = Logger.getLogger(UserEndpoint.class);

    @GET
    @Path("all")
    @RolesAllowed("admin")
    @Operation(summary = "Returns all User as Json List")
    public List<ShopUser> getAllUser() {
        LOG.info("Liste Aller Benutzer abgefragt");
        return ShopUser.listAll(Sort.by("id"));
    }

    @GET
    @RolesAllowed({"admin", "user", "employee"})
    @Operation(summary = "Returns a single User identified by id")
    public ShopUser getCurrentUser(@Context SecurityContext securityContext) {
        return ShopUser.findByEmail(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Transactional
    @PermitAll
    @Operation(summary = "registers a new User in Database")
    public Response registerNewUser(@NotNull ShopUser shopUser) {
        if (ShopUser.findByEmail(shopUser.email) == null) {
            shopUser.role = ShopUser.Role.USER;
            if (!shopUser.checkIfUserIsCorrect()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new RestError(shopUser, "Parameters are Missing")).build();
            }
            shopUser.persist();
            LOG.info("added: " + shopUser.toString());
            return Response.ok().entity(shopUser).build();
        } else
            return Response.status(Response.Status.CONFLICT).entity(new RestError(shopUser, "user Conflict")).build();
    }

    @PUT
    @Transactional
    @RolesAllowed({"user", "admin", "employee"})
    @Operation(summary = "modifies a User")
    public Response updateUser(@NotNull ShopUser newUserInformation, @Context SecurityContext securityContext) {

        if (newUserInformation.id == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new RestError(newUserInformation, "id is missing")).build();

        ShopUser requestingUser = ShopUser.findByEmail(securityContext.getUserPrincipal().getName());
        ShopUser changedUser;

        changedUser = ShopUser.findById(newUserInformation.id);
        if (changedUser == null || requestingUser == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new RestError(newUserInformation, "user not found")).build();


        PermissionUtil permissionUtil = new PermissionUtil(requestingUser, newUserInformation);
        if (!changedUser.equals(newUserInformation) && newUserInformation.checkIfUserIsCorrect()) {
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
            } else
                return Response.status(Response.Status.FORBIDDEN).entity(new RestError(requestingUser, "insufficient privileges or incorrect Information provided")).build();
        }

        if(newUserInformation.role != changedUser.role)
        {
            if(permissionUtil.checkIfAdmin()){
                changedUser.role = newUserInformation.role;
            }
            else {
                return Response.status(Response.Status.FORBIDDEN).entity(newUserInformation).build();
            }
        }

        return Response.ok().entity(ShopUser.findById(changedUser.id)).build();
    }

    @Path("{email}")
    @DELETE
    @RolesAllowed({"user", "admin", "employee"})
    @Transactional
    @Operation(summary = "deletes a user identified by id if permissions are correct")
    public Boolean deleteUser(@PathParam String email, @Context SecurityContext securityContext) {
        ShopUser deleted = ShopUser.findByEmail(email);
        ShopUser shopUser = ShopUser.findByEmail(securityContext.getUserPrincipal().getName());
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
