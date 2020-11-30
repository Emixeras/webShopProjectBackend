package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class ShopOrderUser extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String password;
    @Column(nullable = false)
    public ShopUser.Title title;
    @Column(nullable = false)
    public String firstName;
    @Column(nullable = false)
    public String lastName;
    @Column(nullable = false)
    public String street;
    @Column(nullable = false)
    public int streetNumber;
    @Column(nullable = false)
    public String town;
    @Column(nullable = false)
    public int postalCode;
    @Column(nullable = false)
    public Date birth;

    public ShopOrderUser() {
    }

    public ShopOrderUser(ShopUser shopUser) {
        this.password = shopUser.password;
        this.title = shopUser.title;
        this.firstName = shopUser.firstName;
        this.lastName = shopUser.lastName;
        this.street = shopUser.street;
        this.streetNumber = shopUser.streetNumber;
        this.town = shopUser.town;
        this.postalCode = shopUser.postalCode;
        this.birth = shopUser.birth;
    }

    @OneToMany(mappedBy = "shopOrderUser")
    @JsonbTransient
    private Collection<ShopOrder> shopOrder;

    public Collection<ShopOrder> getShopOrder() {
        return shopOrder;
    }

    public void setShopOrder(Collection<ShopOrder> shopOrder) {
        this.shopOrder = shopOrder;
    }
}
