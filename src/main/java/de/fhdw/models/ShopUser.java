package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;


@Entity
public class ShopUser extends PanacheEntity {
    public enum Role{
        admin, user
    };
    @Column(nullable = false, unique = true)
    public String username;
    @Column(nullable = false)
    public String password;
    public String street, country, postalCode;
    public Date birth;
    @Column(nullable = false)
    public Role role;

    public static ShopUser findByName(String name){
        return find("username", name).firstResult();
    }

}
