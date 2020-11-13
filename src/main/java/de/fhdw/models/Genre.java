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

    @JsonbTransient
    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
