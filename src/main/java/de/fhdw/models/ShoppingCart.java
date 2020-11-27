package de.fhdw.models;

import java.util.List;

public class ShoppingCart {

    public List<ShoppingCartEntries> shoppingCartEntries;

    public ShopOrder.paymentMethod paymentMethod;

    public ShopUser shopUser;

    public ShoppingCart(List<ShoppingCartEntries> shoppingCartEntries, ShopOrder.paymentMethod paymentMethod, ShopUser shopUser) {
        this.shoppingCartEntries = shoppingCartEntries;
        this.paymentMethod = paymentMethod;
        this.shopUser = shopUser;
    }

    public ShoppingCart() {
    }
}
