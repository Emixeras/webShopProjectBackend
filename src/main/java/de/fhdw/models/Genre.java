package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Entity
public class Genre extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String name;

    @JsonbTransient
    @OneToMany(mappedBy = "genre")
    public List<Article> articles;

    @JsonbTransient
    @ManyToOne(cascade = CascadeType.MERGE)
    public Picture picture;

    public Genre() {
    }

    public Genre(String name, Picture image) {
        this.name = name;
        this.picture = image;
    }

    public Genre(String name) {
        this.name = name;
    }

    public static Genre findByName(String name) {
        return find("name", name).firstResult();
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
