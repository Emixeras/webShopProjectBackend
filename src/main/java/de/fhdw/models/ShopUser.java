package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity(name = "ShopUser")
@Table(name = "shopuser")
public class ShopUser extends PanacheEntityBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Long id;
    @NotBlank(message = "email may not be Blank")
    @Column(nullable = false, unique = true)
    public String email;
    @Column(nullable = false)
    public String password;
    @Column(nullable = false)
    public Title title;
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


    @Override
    public String toString() {
        return "ShopUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber=" + streetNumber +
                ", town='" + town + '\'' +
                ", postalCode=" + postalCode +
                ", birth=" + birth +
                ", role=" + role +
                '}';
    }

    public enum Role {
        ADMIN, EMPLOYEE, USER
    }

    public enum Title {
        HERR, FRAU, PROFESSOR, DOKTOR, BENUTZERDEFINIERT, APACHE
    }
}
