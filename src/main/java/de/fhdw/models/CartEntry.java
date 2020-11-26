package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class CartEntry extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    public Article article;

    public int quantity;

    public CartEntry(Article article, int i) {
        this.article = article;
        this.quantity = i;
    }

    public CartEntry() {

    }
}
