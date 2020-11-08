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
    public byte[] data;
    @OneToMany(mappedBy = "image")
    public List<Article> article;
    @OneToMany(mappedBy = "image")
    public List<Artist> artist;
    @OneToMany(mappedBy = "image")
    public List<Genre> genre;
    public Picture() {
    }
    public Picture(byte[] data) {
        this.data = data;
    }


}
