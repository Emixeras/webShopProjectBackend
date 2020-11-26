package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class ShopOrderEntry extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    public ShopOrderArticle shopOrderArticle;

    public int quantity;


    public ShopOrderEntry(ShopOrderArticle shopOrderArticle, int i) {
        this.shopOrderArticle = shopOrderArticle;
        this.quantity = i;
    }

    public ShopOrderEntry() {

    }


    @ManyToOne(optional = false)
    @JsonbTransient
    public ShopOrder shopOrders;

}
