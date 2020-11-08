package de.fhdw.endpoints;

import de.fhdw.models.ShopUser;
import io.smallrye.common.constraint.NotNull;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface UserInterface {
    ShopUser get(@Context SecurityContext securityContext);

    ShopUser put(@NotNull ShopUser shopUser, @Context SecurityContext securityContext);

    Boolean delete(@PathParam String email, @Context SecurityContext securityContext);

    ShopUser post(@NotNull ShopUser shopUser);

    List<ShopUser> getAll();

}
