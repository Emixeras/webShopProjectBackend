package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
public class Picture extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public Long id;

    public byte[] value;

}
