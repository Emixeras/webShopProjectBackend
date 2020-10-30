package de.fhdw.endpoints;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import de.fhdw.models.ShopUser;
import io.smallrye.common.constraint.NotNull;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

public interface UserInterface {
    public ShopUser get(@Context SecurityContext securityContext);
    public ShopUser put(@NotNull ShopUser shopUser, @Context SecurityContext securityContext) throws Exception;
    public Boolean delete(@PathParam String email, @Context SecurityContext securityContext);
    public ShopUser promoteToEmployee(@PathParam String email, @Context SecurityContext securityContext);
    public ShopUser promotoToAdmin(@PathParam String email, @Context SecurityContext securityContext);
    public ShopUser post(@NotNull ShopUser shopUser) throws Exception;
    public List<ShopUser> getAll();

}
