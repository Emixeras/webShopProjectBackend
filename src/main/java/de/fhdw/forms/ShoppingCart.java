package de.fhdw.forms;

import de.fhdw.models.ShopOrder;

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

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "shoppingCartEntries=" + shoppingCartEntries +
                ", paymentMethod=" + paymentMethod +
                ", shipping=" + shipping +
                '}';
    }
}
