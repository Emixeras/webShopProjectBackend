package de.fhdw;

import de.fhdw.models.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

public class TestHelper {


    public  void  emptyDatabase() {
        Article.listAll().forEach(PanacheEntityBase::delete);
        ArticlePicture.listAll().forEach(PanacheEntityBase::delete);
        Artist.listAll().forEach(PanacheEntityBase::delete);
        ArtistPicture.listAll().forEach(PanacheEntityBase::delete);
        Genre.listAll().forEach(PanacheEntityBase::delete);
        GenrePicture.listAll().forEach(PanacheEntityBase::delete);
        ShopUser.listAll().forEach(PanacheEntityBase::delete);
        ShopSys.listAll().forEach(PanacheEntityBase::delete);
    }

    public  void emptyUserTable(){
        ShopUser.listAll().forEach(PanacheEntityBase::delete);
    }
}
