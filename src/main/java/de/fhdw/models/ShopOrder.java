package de.fhdw.models;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Cascade;

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
    public List<ShopOrderEntry> cartEntries;


    public ShopOrder(List<ShopOrderEntry> cartEntries, ShopUser shopUser) {

    }

    public ShopOrder() {

    }


    public enum paymentMethod {
        VORKASSSE, RECHNUNG, KREDITKARTE, PAYPAL, BITCOINS
    }
}
