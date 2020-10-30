package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Article extends PanacheEntity {
    public String name;
    public String description;
    public byte[] Picture;
}
