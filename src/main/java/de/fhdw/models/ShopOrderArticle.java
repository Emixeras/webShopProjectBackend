package de.fhdw.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fhdw.util.PictureHandler;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.Base64;
import java.util.Collection;

@Entity
public class ShopOrderArticle extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String description;
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public double ean;
    @Column(nullable = false)
    public double price;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String artist;
    @Column(nullable = false)
    private byte[] picture;
    @OneToMany(mappedBy = "shopOrderArticle")
    @JsonbTransient
    private Collection<ShopOrderEntry> shopOrderEntry;
    public ShopOrderArticle(Article article) {
        this.setPicture(article.articlePicture.rawData);
        this.genre = article.genre.name;
        this.artist = article.artists.name;
        this.description = article.description;
        this.title = article.title;
        this.ean = article.ean;
        this.price = article.price;
    }
    public ShopOrderArticle() {

    }

    public Genre getGenre() {
        return new Genre(this.genre);
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Artist getArtist() {
        return new Artist(this.artist);
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPicture() {
        return Base64.getEncoder().encodeToString(picture);
    }

    public void setPicture(byte[] bytes) {
        PictureHandler pictureHandler = new PictureHandler();
        this.picture = pictureHandler.scaleImage(bytes, 100);
    }


}
