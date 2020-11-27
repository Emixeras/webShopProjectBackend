package de.fhdw.models;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.List;

@Entity
public class ShopOrder extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @ManyToOne
    public ShopUser shopUser;

    @OneToMany(mappedBy = "shopOrders", cascade = CascadeType.ALL)
    public List<ShopOrderEntry> shopOrderEntries;


    public ShopOrder(List<ShopOrderEntry> shopOrderEntries , ShopUser shopUser) {
        this.shopOrderEntries = shopOrderEntries;
        this.shopUser = shopUser;
    }

    public ShopOrder() {

    }

    public static List<ShopOrder> findByName(ShopUser shopUser) {
        return find("shopuser", shopUser).list();
    }



    public enum paymentMethod {
        VORKASSE, RECHNUNG, KREDITKARTE, PAYPAL, BITCOINS
    }
}
