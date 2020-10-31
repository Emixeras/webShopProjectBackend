package de.fhdw.models;
import io.quarkus.hibernate.orm.panache.*;
import javax.persistence.*;
import java.util.*;


@Entity(name = "ShopUser")
@Table(name = "shopuser")
public class ShopUser extends PanacheEntity {

    public enum Role {
        ADMIN, EMPLOYEE, USER
    }

    @Column(nullable = false, unique = true)
    public String email;
    @Column(nullable = false)
    public String password;
    @Column(nullable = false)
    public String firstName;
    @Column(nullable = false)
    public String lastName;
    public String street;
    public int streetNumber;
    public String town;
    public int postalCode;
    @Column(nullable = false)
    public Date birth;
    public Role role;


    public static ShopUser findbyEmail(String name) {
        return find("email", name).firstResult();
    }

}
