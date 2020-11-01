package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class ArticleMetadata extends PanacheEntity {
  //  @Column(nullable = false)
    public String title;
  //  @Column(nullable = false)
    public double price;
    @ManyToOne(cascade = CascadeType.ALL)
    public ArticleGenre articleGenre;
    public int ean;
    public String description;
  //  @ManyToOne()
  //  public ArticleArtist articleArtists;
}
