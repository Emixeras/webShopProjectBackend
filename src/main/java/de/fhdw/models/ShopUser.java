package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;


@Entity(name = "ShopUser")
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


    public boolean checkIfUserIsCorrect() {

        return (email != null && password != null && title != null && this.firstName != null && birth != null && role != null);

    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopUser)) return false;
        ShopUser shopUser = (ShopUser) o;
        return streetNumber == shopUser.streetNumber &&
                postalCode == shopUser.postalCode &&
                id.equals(shopUser.id) &&
                email.equals(shopUser.email) &&
                password.equals(shopUser.password) &&
                title == shopUser.title &&
                firstName.equals(shopUser.firstName) &&
                lastName.equals(shopUser.lastName) &&
                Objects.equals(street, shopUser.street) &&
                Objects.equals(town, shopUser.town) &&
                Objects.equals(birth, shopUser.birth) &&
                role == shopUser.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, title, firstName, lastName, street, streetNumber, town, postalCode, birth, role);
    }



    public enum Role {
        ADMIN, EMPLOYEE, USER
    }

    public enum Title {
        HERR, FRAU, PROFESSOR, DOKTOR, BENUTZERDEFINIERT, APACHE
    }
}
