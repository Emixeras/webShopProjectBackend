package de.fhdw.models;
import io.quarkus.hibernate.orm.panache.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Entity(name = "ShopUser")
@Table(name = "shopuser")
public class ShopUser extends PanacheEntityBase  {


    @Id
    @GeneratedValue
    public Long id;

    public enum Role {
        ADMIN, EMPLOYEE, USER
    }
    @NotBlank(message = "email may not be Blank")
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
    @Column(nullable = false)
    public Role role;

    public static ShopUser findbyEmail(String name) {
        return find("email", name).firstResult();
    }

}
