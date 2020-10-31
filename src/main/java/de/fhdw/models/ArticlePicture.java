package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class ArticlePicture extends PanacheEntity {

    public Byte[] picture;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public ArticleMetadata articleMetadata;
}
