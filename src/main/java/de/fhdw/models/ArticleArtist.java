package de.fhdw.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class ArticleArtist extends PanacheEntity {

    public String name;

  // @OneToMany(mappedBy = "articleArtists" )
  //  public List<ArticleMetadata> articleMetadataList;
}
