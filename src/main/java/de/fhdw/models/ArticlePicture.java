package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
public class ArticlePicture extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonbTransient
    @Column(nullable = false)
    public byte[] rawData;



    public ArticlePicture() {
    }

    public ArticlePicture(byte[] rawData, byte[] thumbnail) {
        this.rawData = rawData;
    }

}
