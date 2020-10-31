package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class ArticleArtist extends PanacheEntity {

    public String name;

}
