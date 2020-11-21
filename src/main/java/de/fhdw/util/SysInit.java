package de.fhdw.util;

import de.fhdw.models.*;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@ApplicationScoped
public class SysInit {
    private static final Logger LOG = Logger.getLogger(SysInit.class);

    @ConfigProperty(name = "demo.data", defaultValue = "false")
    boolean demoData;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOG.info("systable initialized: " + initSysTable());
        if (Boolean.TRUE.equals(demoData)) {
            initDemoData();
        } else {
            LOG.info("demo data will not be initialized");
        }

    }

    private void initDemoData() {
        List<ShopSys> shopSys = ShopSys.listAll();
        shopSys.forEach(i -> {
                    if (Boolean.TRUE.equals(i.initialized)) {
                        LOG.info(i.value + " is initialized: " + i.initialized);
                    } else {
                        switch (i.value) {
                            case "user":
                                if (Boolean.FALSE.equals(i.initialized)) {
                                    LOG.info("initializing user:");
                                    i.initialized = initUser();
                                    LOG.info("DONE " + i.initialized);
                                }
                                break;
                            case "genre":
                                if (Boolean.FALSE.equals(i.initialized)) {
                                    LOG.info("initializing genres:");
                                    i.initialized = initGenre();
                                    LOG.info("DONE " + i.initialized);
                                }
                                break;
                            case "artist":
                                if (Boolean.FALSE.equals(i.initialized)) {
                                    LOG.info("initializing artists:");
                                    i.initialized = initArtist();
                                    LOG.info("DONE " + i.initialized);
                                }
                                break;
                            case "article":
                                if (Boolean.FALSE.equals(i.initialized)) {
                                    LOG.info("initializing Articles:");
                                    i.initialized = initArticles();
                                    LOG.info("DONE " + i.initialized);
                                }
                                break;
                            default:
                                LOG.error("Error in System Table" + i.value + "not recognized");
                                System.exit(1);
                        }

                    }
                }
        );
    }

    private boolean initSysTable() {
        List<ShopSys> shopSys = new ArrayList<>();
        shopSys.add(ShopSys.findByName("user") != null ? ShopSys.findByName("user") : new ShopSys("user", false));
        shopSys.add(ShopSys.findByName("artist") != null ? ShopSys.findByName("artist") : new ShopSys("artist", false));
        shopSys.add(ShopSys.findByName("genre") != null ? ShopSys.findByName("genre") : new ShopSys("genre", false));
        shopSys.add(ShopSys.findByName("article") != null ? ShopSys.findByName("article") : new ShopSys("article", false));
        shopSys.forEach(i -> {
            if (!i.isPersistent()) {
                i.persist();
            }
        });
        return true;
    }

    @Transactional
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
            LOG.info(ShopUser.count() + " Benutzer angelegt");
        } catch (Exception e) {
            LOG.error("Fehler bei Benutzer Initialisierung: " + e.toString());
        }
        return true;
    }

    @Transactional
    public boolean initGenre() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            List<Genre> genres = jsonb.fromJson(getClass().getResourceAsStream("/TestData/genre.json"), new ArrayList<Genre>() {
            }.getClass().getGenericSuperclass());
            genres.forEach(i -> {
                String name = "/TestData/GenreImages/genre" + i.id + ".png";
                PictureHandler pictureHandler = new PictureHandler();
                GenrePicture genrePicture = null;
                try {
                    genrePicture = new GenrePicture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert genrePicture != null;
                genrePicture.persist();
                Genre genre = new Genre(i.name);
                genre.picture = genrePicture;
                genre.persist();
                LOG.debug("added Genres: " + genre.toString());
            });
            LOG.info(Genre.count() + " Genres angelegt");
            return true;
        } catch (Exception e) {
            LOG.error("Fehler beim Genre erstellen" + e.toString());
            return false;
        }
    }

    @Transactional
    public boolean initArtist() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            //add Artists
            List<Artist> artists = jsonb.fromJson(getClass().getResourceAsStream("/TestData/artist.json"), new ArrayList<Artist>() {
            }.getClass().getGenericSuperclass());
            artists.forEach(i -> {
                String name = "/TestData/ArtistImages/artist (" + i.id + ").jpg";
                ArtistPicture artistPicture = null;
                try {
                    artistPicture = new ArtistPicture(IOUtils.toByteArray(getClass().getResourceAsStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert artistPicture != null;
                artistPicture.persist();
                Artist artist = new Artist(i.name, artistPicture );
                artist.persist();
                LOG.debug("added Artist: " + artist.toString());
            });
            LOG.info(Artist.count() + " Artists angelegt");
        } catch (Exception e) {
            LOG.error("Fehler beim Artist erstellen: " + e.toString());
            return false;
        }
        return true;

    }

    @Transactional
    public boolean initArticles() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            //put Article Pictures in DB
            IntStream.range(1, 501).forEach(i -> {
                String name = "/TestData/ArticleImages/cover (" + i + ").jpg";
                LOG.debug(i + " " + name);
                PictureHandler pictureHandler = new PictureHandler();
                ArticlePicture articlePicture = null;
                try {
                    articlePicture = new ArticlePicture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert articlePicture != null;
                articlePicture.persist();
                LOG.debug(name);
            });

            LOG.debug(ArticlePicture.count() + " Bilder angelegt");
            //add Articles
            List<Article> articles = jsonb.fromJson(getClass().getResourceAsStream("/TestData/article.json"), new ArrayList<Article>() {
            }.getClass().getGenericSuperclass());
            articles.forEach(i -> {
                Article article = new Article();
                LOG.debug(i.articlePicture);
                article.articlePicture = i.articlePicture;
                article.title = i.title;
                article.genre = i.genre;
                article.artists = i.artists;
                article.description = i.description;
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
