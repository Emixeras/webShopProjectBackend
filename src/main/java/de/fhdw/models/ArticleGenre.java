package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ArticleGenre extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String name;
    @Column(nullable = false)
    public String description;


    public static ShopUser findByName(String name) {
        return find("name", name).firstResult();
    }


}
