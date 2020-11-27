package de.fhdw.models;

import java.util.List;

public class ShoppingCart {

    public List<ShoppingCartEntries> shoppingCartEntries;

    public ShopOrder.paymentMethod paymentMethod;

    public double shipping;

    public ShoppingCart(List<ShoppingCartEntries> shoppingCartEntries, ShopOrder.paymentMethod paymentMethod, int shipping) {
        this.shoppingCartEntries = shoppingCartEntries;
        this.paymentMethod = paymentMethod;
        this.shipping = shipping;
    }

    public ShoppingCart() {
    }
}
