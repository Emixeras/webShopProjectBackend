package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity(name = "Address")
@Table(name = "address")
public class Address extends PanacheEntity {


    public Address() {
    }
    @Column(nullable = false)
    public String street;
    public String country;
    @Column(nullable = false)
    public int postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    public ShopUser shopUser;

}
