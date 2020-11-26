package de.fhdw.endpoints;

import de.fhdw.models.ShopOrder;
import de.fhdw.models.ShopOrderEntry;
import de.fhdw.models.ShopUser;
import de.fhdw.util.RestError;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public class OrderEndpoint {

    @Transactional
    @RolesAllowed({"user", "employee", "admin"})
    public Response createOrder(List<ShopOrderEntry> cartEntries, @Context SecurityContext securityContext) {

        ShopUser shopUser = ShopUser.findByEmail(securityContext.getUserPrincipal().getName());

        if(shopUser == null)
            return Response.status(Response.Status.FORBIDDEN).entity(new RestError(cartEntries, "Benutzer nicht gefunden")).build();

        ShopOrder shopOrder = new ShopOrder();
        shopOrder.shopUser = shopUser;
        shopOrder.persist();
        cartEntries.forEach(i -> {
            i.shopOrders = shopOrder;
            i.persist();
        });

        return Response.ok().entity(shopOrder).build();
    }

    public void getOrder() {
    }
}
