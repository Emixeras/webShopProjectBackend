package de.fhdw.util;

import de.fhdw.models.*;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@ApplicationScoped
public class SysInit {
    private static final Logger LOG = Logger.getLogger(SysInit.class);

    void onStart(@Observes StartupEvent event) {
        LOG.info("Systemtabelle aufgebaut: " + initSysTable());
        List<ShopSys> shopSys = ShopSys.listAll();
        shopSys.forEach(i -> {
            if (i.initialized) {
                LOG.info(i.value + " ist initialisiert:" + i.value);
            }
            switch (i.value) {
                case "user":
                    if (i.initialized) {
                        initUser();
                        i.initialized = true;
                        LOG.info("Benutzer wurden initialisiert");
                    }
                case "genre":
                    if (i.initialized) {
                        initGenre();
                        i.initialized = true;
                        LOG.info("Artikel wurden initialisiert");
                    }
                case "article":
                    if (i.initialized) {
                        initArticles();
                        i.initialized = true;
                        LOG.info("Artikel wurden initialisiert");
                    }

            }

        });
    }

    private boolean initSysTable() {
        List<ShopSys> shopSys = new ArrayList<>();
        shopSys.add(ShopSys.findByName("user") != null ? ShopSys.findByName("user") : new ShopSys("user", false));
        shopSys.add(ShopSys.findByName("user") != null ? ShopSys.findByName("article") : new ShopSys("article", false));
        shopSys.add(ShopSys.findByName("user") != null ? ShopSys.findByName("artist") : new ShopSys("artist", false));
        shopSys.add(ShopSys.findByName("user") != null ? ShopSys.findByName("genre") : new ShopSys("genre", false));
        shopSys.forEach(i -> {
            if (!i.isPersistent()) {
                i.persist();
            }
        });
        return true;
    }

    public boolean initUser() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            //add Users
            List<ShopUser> user = jsonb.fromJson(getClass().getResourceAsStream("/TestData/user.json"), new ArrayList<ShopUser>() {
            }.getClass().getGenericSuperclass());
            user.forEach(i -> {
                ShopUser shopUser = new ShopUser();
                shopUser.birth = i.birth;
                shopUser.email = i.email;
                shopUser.firstName = i.firstName;
                shopUser.lastName = i.lastName;
                shopUser.password = i.password;
                shopUser.postalCode = i.postalCode;
                shopUser.role = i.role;
                shopUser.street = i.street;
                shopUser.streetNumber = i.streetNumber;
                shopUser.title = i.title;
                shopUser.town = i.town;
                shopUser.persist();
                LOG.debug("added User: " + shopUser.toString());
            });
            LOG.debug(ShopUser.count() + " Benutzer angelegt");
        } catch (Exception e) {
            LOG.error("Fehler bei Benutzer Initialisierung: " + e.toString());
        }
        return true;
    }

    public boolean initGenre() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            List<Genre> genres = jsonb.fromJson(getClass().getResourceAsStream("/TestData/genre.json"), new ArrayList<Genre>() {
            }.getClass().getGenericSuperclass());
            genres.forEach(i -> {
                String name = "/TestData/genre/genre" + i.id + ".png";
                PictureHandler pictureHandler = new PictureHandler();
                Picture picture = null;
                try {
                    picture = new Picture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                picture.persist();
                Genre genre = new Genre(i.name);
                genre.picture = picture;
                genre.persist();
                LOG.debug("added Genres: " + genre.toString());
            });
            LOG.debug(Genre.count() + " Genres angelegt");
            return true;
        } catch (Exception e) {
            LOG.error("Fehler beim Genre erstellen" + e.toString());
            return false;
        }
    }

    public boolean initArtist() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            //add Artists
            List<Artist> artists = jsonb.fromJson(getClass().getResourceAsStream("/TestData/artist.json"), new ArrayList<Artist>() {
            }.getClass().getGenericSuperclass());
            artists.forEach(i -> {
                Artist artist = new Artist(i.name);
                artist.persist();
                LOG.debug("added Genres: " + artist.toString());
            });
            LOG.info(Artist.count() + " Artists angelegt");
        } catch (Exception e) {
            LOG.error("Fehler beim Artist erstellen: " + e.toString());
            return false;
        }
        return true;
    }


    public boolean initArticles() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            //put Article Pictures in DB
            IntStream.range(1, 11).forEach(i -> {
                String name = "/TestData/Images/cat" + i + ".jpg";

                LOG.debug(i + " " + name);
                PictureHandler pictureHandler = new PictureHandler();
                Picture picture = null;
                try {
                    picture = new Picture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                picture.persist();
            });
            LOG.info(Picture.count() + " Bilder angelegt");
            //add Articles
            List<Article> articles = jsonb.fromJson(getClass().getResourceAsStream("/TestData/article.json"), new ArrayList<Article>() {
            }.getClass().getGenericSuperclass());
            articles.forEach(i -> {
                Article article = new Article();
                LOG.info(i.picture);
                article.picture = i.picture;
                article.title = i.title;
                article.genre = i.genre;
                article.artists = i.artists;
                article.price = i.price;
                article.ean = i.ean;
                article.persist();
                LOG.debug("added article: " + article.toString());
            });
            LOG.info(Article.count() + " Artikel angelegt");
        } catch (Exception e) {
            LOG.error("Fehler beim Artikel erstellen " + e.toString());
            return false;
        }
        return true;
    }


}
