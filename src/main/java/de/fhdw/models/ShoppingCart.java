package de.fhdw.models;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity
public class ShoppingCart extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @ManyToOne
    public ShopUser shopUser;

    @OneToMany
    public List<CartEntry> cartEntries;


    public ShoppingCart(List<CartEntry> cartEntries, ShopUser shopUser) {

    }

    public ShoppingCart() {

    }


    public enum paymentMethod {
        VORKASSSE, RECHNUNG, KREDITKARTE, PAYPAL, BITCOINS
    }
}
