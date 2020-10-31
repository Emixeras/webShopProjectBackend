package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class ArticleGenre extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String name;
    @Column(nullable = false)
    public String description;

    @ManyToOne()
    public ArticleMetadata articleMetadataList;

    public static ShopUser findByName(String name) {
        return find("name", name).firstResult();
    }


}
