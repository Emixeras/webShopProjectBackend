package de.fhdw.endpoints;

import de.fhdw.models.Article;
import de.fhdw.models.ShopOrder;
import de.fhdw.models.ShopUser;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public class OrderImpl implements OrderInterface {
    @Override
    public List<ShopOrder> getAllOrdersForUser(ShopUser shopUser, SecurityContext securityContext) {
        ShopUser requestingUser = ShopUser.findbyEmail(securityContext.getUserPrincipal().getName());

        if (requestingUser.email.equals(securityContext.getUserPrincipal().getName()) || requestingUser.role == ShopUser.Role.ADMIN || requestingUser.role == ShopUser.Role.EMPLOYEE) {
            return ShopOrder.list("select * from order where shopuser_id = ", requestingUser.id);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }

    }

    @Override
    public ShopOrder orderNewArticle(List<Article> articles, SecurityContext securityContext) {
        return null;
    }
}
