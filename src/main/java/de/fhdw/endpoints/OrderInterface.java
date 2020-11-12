package de.fhdw.endpoints;

import de.fhdw.models.Article;
import de.fhdw.models.ShopOrder;
import de.fhdw.models.ShopUser;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface OrderInterface {

    List<ShopOrder> getAllOrdersForUser(ShopUser shopUser, @Context SecurityContext securityContext);

    ShopOrder orderNewArticle(List<Article>articles, @Context SecurityContext securityContext);



}
