package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Entity
public class ArticleGenre extends PanacheEntity {

    public ArticleGenre() {
    }

    @Column(nullable = false, unique = true)
    public String name;
    @Column(nullable = false)
    public String description;


    @OneToMany(
            mappedBy = "articleGenre",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonbTransient
    public List<ArticleMetadata> articleMetadata;

    public static ArticleGenre findByName(String name) {
        return find("name", name).firstResult();
    }


}
