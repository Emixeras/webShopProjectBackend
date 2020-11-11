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
    @OneToMany(mappedBy = "picture")
    public List<Article> article;
    @JsonbTransient
    @OneToMany(mappedBy = "picture")
    public List<Artist> artist;
    @JsonbTransient
    @OneToMany(mappedBy = "picture")
    public List<Genre> genre;

    public Picture() {
    }

    public Picture(byte[] rawData, byte[] thumbnail) {
        this.rawData = rawData;
        this.thumbnail = thumbnail;
    }

    public void setId(Long id) {
    }

    @Override
    public String toString() {
       return id.toString();
    }
}
