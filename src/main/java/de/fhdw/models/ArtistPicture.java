package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ArtistPicture extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonbTransient
    public byte[] rawData;

    @JsonbTransient
    public byte[] thumbnail;


    public ArtistPicture() {
    }

    public ArtistPicture(byte[] rawData, byte[] thumbnail) {
        this.rawData = rawData;
        this.thumbnail = thumbnail;
    }

}
