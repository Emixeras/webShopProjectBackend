package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;

@Entity
public class ArticleMetadata extends PanacheEntity {
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public String artist;
    @Column(nullable = false)
    public double price;
    @ManyToMany
    @Column(nullable = false)
    public List<ArticleGenre> articleGenres;
    public int ean;
    public String description;


}
