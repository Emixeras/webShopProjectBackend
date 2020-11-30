package de.fhdw.models;


import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class ShopOrder extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    public ShopOrderUser shopOrderUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonbTransient
    public ShopUser shopUser;

    @OneToMany(mappedBy = "shopOrders", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    @JsonbProperty("entries")
    public List<ShopOrderEntry> shopOrderEntries;

    public Date orderDate;
    public double shipping;

    public Payment payment;

    public ShopOrder(List<ShopOrderEntry> shopOrderEntries, ShopOrderUser shopUser) {
        this.shopOrderEntries = shopOrderEntries;
        this.shopOrderUser = shopUser;
    }

    public ShopOrder() {

    }


    public static List<ShopOrder> findByName(ShopUser shopUser) {
        return find("shopuser", shopUser).list();
    }

    public enum Payment {
        VORKASSE, RECHNUNG, KREDITKARTE, PAYPAL, BITCOINS
    }
}
