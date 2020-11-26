package de.fhdw.models;

import de.fhdw.util.PictureHandler;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.util.Collection;

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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] bytes) {
        PictureHandler pictureHandler = new PictureHandler();
        this.picture = pictureHandler.scaleImage(bytes, 100);
    }

    @OneToMany(mappedBy = "shopOrderArticle")
    private Collection<ShopOrderEntry> shopOrderEntry;

    public Collection<ShopOrderEntry> getShopOrderEntry() {
        return shopOrderEntry;
    }

    public void setShopOrderEntry(Collection<ShopOrderEntry> shopOrderEntry) {
        this.shopOrderEntry = shopOrderEntry;
    }
}
