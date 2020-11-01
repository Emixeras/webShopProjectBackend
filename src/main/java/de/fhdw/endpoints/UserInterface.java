package de.fhdw.endpoints;

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import de.fhdw.models.ShopUser;
import io.smallrye.common.constraint.NotNull;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

public interface UserInterface {
    public ShopUser get(@Context SecurityContext securityContext);
    public ShopUser put(@NotNull ShopUser shopUser, @Context SecurityContext securityContext) ;
    public Boolean delete(@PathParam String email, @Context SecurityContext securityContext);
    public ShopUser post(@NotNull ShopUser shopUser);
    public List<ShopUser> getAll();

}
