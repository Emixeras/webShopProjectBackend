package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class ArticleMetadata extends PanacheEntity {
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public double price;
    @OneToMany(mappedBy = "articleMetadataList", fetch = FetchType.EAGER, orphanRemoval = false)
    @Column(nullable = false)
    public List<ArticleGenre> articleGenres;
    public int ean;
    public String description;
    @ManyToOne()
    public ArticleArtist articleArtists;
}
