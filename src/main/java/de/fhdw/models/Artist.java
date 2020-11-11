package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Entity
public class Artist extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(unique = true, nullable = false)
    public String name;
    @OneToMany(
            mappedBy = "artists",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JsonbTransient
    public List<Article> articleList;
    @JsonbTransient
    @ManyToOne(cascade = CascadeType.MERGE)
    public Picture picture;
    public Artist(String name) {
        this.name = name;
    }

    public Artist() {
    }

    public static Artist findByName(String name) {
        return find("name", name).firstResult();
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
