package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
public class ShopUser extends PanacheEntity {

    public enum Role {
        ADMIN, USER
    }

    @Column(nullable = false, unique = true)
    public String username;
    @Column(nullable = false)
    public String password;
    public String firstName;
    public String lastName;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Column(nullable = true)
    public List<Address> addresses;
    public Date birth;

    @Column(nullable = false)
    public Role role;


    public static ShopUser findByName(String name) {
        return find("username", name).firstResult();
    }

}
