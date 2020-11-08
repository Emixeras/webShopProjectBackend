package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.List;

@Entity
public class Picture extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @JsonbTransient
    public byte[] rawData;
    @JsonbTransient
    public byte[] thumbnail;
    @JsonbTransient
    @OneToMany(mappedBy = "image")
    public List<Article> article;
    @JsonbTransient
    @OneToMany(mappedBy = "image")
    public List<Artist> artist;
    @JsonbTransient
    @OneToMany(mappedBy = "image")
    public List<Genre> genre;

    public Picture() {
    }

    public Picture(byte[] rawData, byte[] thumbnail) {
        this.rawData = rawData;
        this.thumbnail = thumbnail;
    }
}
