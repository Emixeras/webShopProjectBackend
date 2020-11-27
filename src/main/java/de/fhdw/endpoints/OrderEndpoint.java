package de.fhdw.endpoints;

import de.fhdw.models.*;
import de.fhdw.util.RestError;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("order")
@Tag(name = "Order", description = "Operations for User Order")
public class OrderEndpoint {

    private static final Logger LOG = Logger.getLogger(OrderEndpoint.class);

    @Transactional
    @POST
    @RolesAllowed({"user", "employee", "admin"})
    @Tag(name = "new Order", description = "Generates new Order, needs authentication")
    public Response createOrder(ShoppingCart shoppingCart, @Context SecurityContext securityContext) {
        ShopUser shopUser = ShopUser.findByEmail(securityContext.getUserPrincipal().getName());
        if (shopUser == null)
            return Response.status(Response.Status.FORBIDDEN).entity(new RestError(shoppingCart, "Benutzer nicht gefunden")).build();
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.shopUser = shopUser;
        shopOrder.persist();
        shoppingCart.shoppingCartEntries.forEach(i -> {
            ShopOrderArticle shopOrderArticle = new ShopOrderArticle(Article.findById(i.article.id));
            ShopOrderEntry shopOrderEntry = new ShopOrderEntry(shopOrderArticle, i.count);
            shopOrderEntry.shopOrders = shopOrder;
            LOG.info(shopOrderEntry.toString());
            shopOrderEntry.persist();
        });
        return Response.ok().entity(shopOrder).build();
    }

    @GET
    @RolesAllowed({"user", "employee", "admin"})
    @Tag(name = "get Orders for LoggedIn User", description = "gets The Orders for the User Currently logged in")
    public Response getOrderForUser(@Context SecurityContext securityContext) {
        ShopUser requestingUser = ShopUser.findByEmail(securityContext.getUserPrincipal().getName());

        if (requestingUser.shopOrder.isEmpty())
            return Response.status(Response.Status.NO_CONTENT).build();

        return Response.status(Response.Status.OK).entity(requestingUser.shopOrder).build();
    }

    @GET
    public ShoppingCart GetExampleShoppingCart() {
        List<ShoppingCartEntries> shoppingCartEntriesArrayList = new ArrayList<>();
        ShoppingCartEntries shoppingCartEntries = new ShoppingCartEntries(Article.findById(1L), 5);
        shoppingCartEntriesArrayList.add(shoppingCartEntries);
        return new ShoppingCart(shoppingCartEntriesArrayList, ShopOrder.paymentMethod.PAYPAL, 15);
    }
}
