package de.fhdw.models;

import java.util.List;

public class ShoppingCart {

    public List<ShoppingCartEntries> shoppingCartEntries;

    public ShopOrder.Payment paymentMethod;

    public double shipping;


    public ShoppingCart(List<ShoppingCartEntries> shoppingCartEntries, ShopOrder.Payment paymentMethod, int shipping) {
        this.shoppingCartEntries = shoppingCartEntries;
        this.paymentMethod = paymentMethod;
        this.shipping = shipping;
    }

    public ShoppingCart() {
    }
}
