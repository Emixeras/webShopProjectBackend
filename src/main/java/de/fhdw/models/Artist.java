package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class Artist extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String name;

    @ManyToOne
    public ArtistPicture picture;

    public Artist(String name) {
        this.name = name;
    }

    public Artist() {
    }

    public static Artist findByName(String name) {
        return find("name", name).firstResult();
    }

    @JsonbTransient
    public ArtistPicture getPicture() {
        return picture;
    }

    public void setPicture(ArtistPicture artistPicture) {
        this.picture = artistPicture;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
