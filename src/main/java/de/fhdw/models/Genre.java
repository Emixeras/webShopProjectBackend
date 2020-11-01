package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Entity
public class Genre extends PanacheEntity {

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    @Column(nullable = false, unique = true)
    public String name;


    @OneToMany(
            mappedBy = "genre"
    )
    @JsonbTransient
    public List<Article> articles;
    public static Genre findByName(String name) {
        return find("name", name).firstResult();
    }


}
