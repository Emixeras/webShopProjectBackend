package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class Genre extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    public GenrePicture picture;

    public Genre() {
    }

    public Genre(String name, GenrePicture image) {
        this.name = name;
        this.picture = image;
    }

    public Genre(String name) {
        this.name = name;
    }

    public static Genre findByName(String name) {
        return find("name", name).firstResult();
    }

    @JsonbTransient
    public GenrePicture getPicture() {
        return picture;
    }

    public void setPicture(GenrePicture genrePicture) {
        this.picture = genrePicture;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
