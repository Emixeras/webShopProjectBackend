package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@NamedQuery(name = "Article.getRange", query = "FROM Article AS a WHERE a.id BETWEEN :sID and :eID")
public class Article extends PanacheEntityBase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public double price;

    public double ean;

    public String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    public Genre genre;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    public Artist artists;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    public ArticlePicture articlePicture;

    public static Artist findByName(String title) {
        return find("title", title).firstResult();
    }

    public static Artist findRange(long start, long end) {
        return find("select * f").firstResult();
    }

    @JsonbTransient
    public ArticlePicture getPicture() {
        return articlePicture;
    }

    public void setPicture(ArticlePicture articlePicture) {
        this.articlePicture = articlePicture;
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
