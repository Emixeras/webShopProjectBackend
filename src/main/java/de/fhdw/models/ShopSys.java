package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShopSys extends PanacheEntityBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String value;
    public Boolean initialized;

    public ShopSys() {
    }

    public ShopSys(String value, Boolean initialized) {
        this.value = value;
        this.initialized = initialized;
    }

    public static ShopSys findByName(String value) {
        return find("value", value).firstResult();
    }
}
