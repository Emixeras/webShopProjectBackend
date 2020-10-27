package de.fhdw.endpoints;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import de.fhdw.models.ShopUser;
import io.smallrye.common.constraint.NotNull;

public interface UserInterface {
    public ShopUser get(@PathParam String username, @Context SecurityContext securityContext);
    public ShopUser put(@PathParam String username, @NotNull ShopUser shopUser, @Context SecurityContext securityContext) throws Exception;
    public Boolean delete(@PathParam String username);
    public List<ShopUser> getAll();
    public ShopUser post(@PathParam String username, @NotNull ShopUser shopUser) throws Exception;
    
}
