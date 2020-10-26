package de.fhdw.endpoints;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import de.fhdw.models.ShopUser;
import io.smallrye.common.constraint.NotNull;

public interface UserInterface {
    public ShopUser login(@Context SecurityContext securityContext);
    public ShopUser edit(@NotNull ShopUser shopUser, @Context SecurityContext securityContext) throws Exception;
    public List<ShopUser> getAll();
    public ShopUser register(@NotNull ShopUser shopUser) throws Exception;
    
}
