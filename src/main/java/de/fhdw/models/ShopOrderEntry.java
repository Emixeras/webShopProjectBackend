package de.fhdw.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class ShopOrderEntry extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JsonbProperty("count")
    public ShopOrderArticle shopOrderArticle;


    @JsonbProperty("count")
    public int quantity;
    @ManyToOne(optional = false)
    @JsonbTransient
    public ShopOrder shopOrders;

    public ShopOrderEntry(ShopOrderArticle shopOrderArticle, int i) {
        this.shopOrderArticle = shopOrderArticle;
        this.quantity = i;
    }


    public ShopOrderEntry() {

    }

    @Override
    public String toString() {
        return "ShopOrderEntry{" +
                "id=" + id +
                ", shopOrderArticle=" + shopOrderArticle +
                ", quantity=" + quantity +
                ", shopOrders=" + shopOrders +
                '}';
    }
}
