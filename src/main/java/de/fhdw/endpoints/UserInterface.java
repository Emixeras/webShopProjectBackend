package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import io.smallrye.common.constraint.NotNull;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface UserInterface {
    ShopUser getCurrentUser(@Context SecurityContext securityContext);

    ShopUser updateUser(@NotNull ShopUser shopUser, @Context SecurityContext securityContext);

    Boolean deleteUser(@PathParam String email, @Context SecurityContext securityContext);

    ShopUser registerNewUser(@NotNull ShopUser shopUser);

    List<ShopUser> getAllUser();

}
