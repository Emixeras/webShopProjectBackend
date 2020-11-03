package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Entity
public class Artist extends PanacheEntityBase {
    public Artist(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Long id;

    public Artist() {
    }
    @Column(unique = true, nullable = false)
    public String name;

    @OneToMany(
            mappedBy = "artists",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonbTransient
    public List<Article> articleList;
    public static Artist findByName(String name) {
        return find("name", name).firstResult();
    }

}
