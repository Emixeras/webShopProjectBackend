package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Address extends PanacheEntity {
    public String street, country;
    public int postalCode;
}
