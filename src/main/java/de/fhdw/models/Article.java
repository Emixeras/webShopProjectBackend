package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class Article extends PanacheEntity {
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public double price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    public Genre genre;
    public int ean;
    public String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    public Artist artists;

    @JsonbTransient
    @OneToOne
   // @JoinColumn(nullable = false) //todo: Uncomment after tests are finished
    public Picture picture;

    public static Artist findbyName(String title) {
        return find("title", title).firstResult();
    }

}
