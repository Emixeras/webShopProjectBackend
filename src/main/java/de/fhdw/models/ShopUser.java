package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class ShopUser extends PanacheEntity {



    public String username, password, street, country, postalCode;
    public Date birth;

}
