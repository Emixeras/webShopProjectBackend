package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@NamedQuery(name = "Artist.getRange", query = "FROM Artist AS a WHERE a.id BETWEEN :sID and :eID")
public class Artist extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(unique = true, nullable = false)
    public String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    public ArtistPicture picture;

    public Artist(String name) {
        this.name = name;
    }

    public Artist() {
    }

    public Artist(String name, ArtistPicture picture) {
        this.name = name;
        this.picture = picture;
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
