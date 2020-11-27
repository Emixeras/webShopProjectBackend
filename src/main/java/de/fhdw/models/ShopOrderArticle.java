package de.fhdw.models;

import de.fhdw.util.PictureHandler;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Entity
public class ShopOrderArticle extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String articleName;
    @Column(nullable = false)
    public String genreName;
    @Column(nullable = false)
    public String description;
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public int ean;
    @Column(nullable = false)
    public double price;
    @Column(nullable = false)
    private byte[] picture;

    public ShopOrderArticle(Article article) {
        this.setPicture(article.articlePicture.rawData);
        this.articleName = article.title;
        this.genreName = article.genre.name;
        this.description = article.description;
        this.title = article.title;
        this.ean = article.ean;
        this.price = article.price;
    }

    public ShopOrderArticle() {

    }

    @JsonbTransient
    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] bytes) {
        PictureHandler pictureHandler = new PictureHandler();
        this.picture = pictureHandler.scaleImage(bytes, 100);
    }

    @OneToMany(mappedBy = "shopOrderArticle")
    @JsonbTransient
    private Collection<ShopOrderEntry> shopOrderEntry;





}
