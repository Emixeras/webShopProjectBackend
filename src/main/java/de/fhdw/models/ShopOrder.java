package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;
import java.util.List;

@Entity
public class ShopOrder extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String test;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "shopOrder")
    List<ShopOrderItem> orderItems;


    @ManyToOne
    @JoinColumn(nullable = false)
    ShopUser shopUser;

}
