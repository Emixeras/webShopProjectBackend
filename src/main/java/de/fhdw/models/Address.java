package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Address extends PanacheEntity {
    public Address(String street, String country, int postalCode) {
        this.street = street;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Address() {
    }
    @Column(nullable = false)
    public String street;
    public String country;
    @Column(nullable = false)
    public int postalCode;
}
