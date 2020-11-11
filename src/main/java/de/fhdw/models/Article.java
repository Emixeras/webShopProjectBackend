package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class Article extends PanacheEntityBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public double price;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    public Genre genre;

    public int ean;

    public String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    public Artist artists;

    @JsonbTransient
    @ManyToOne()
    public Picture picture;

    public static Artist findByName(String title) {
        return find("title", title).firstResult();
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", genre=" + genre +
                ", ean=" + ean +
                ", description='" + description + '\'' +
                ", artists=" + artists +
                '}';
    }
}
