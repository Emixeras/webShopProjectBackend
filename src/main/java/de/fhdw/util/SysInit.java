package de.fhdw.util;

import de.fhdw.endpoints.OrderEndpoint;
import de.fhdw.forms.ShoppingCart;
import de.fhdw.forms.ShoppingCartEntries;
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
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


@ApplicationScoped
public class SysInit {
    private static final Logger LOG = Logger.getLogger(SysInit.class);

    @ConfigProperty(name = "demo.data.lazy", defaultValue = "true")
    public boolean lazyDemoData;

    @ConfigProperty(name = "demo.data", defaultValue = "false")
    public boolean demoData;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (Boolean.TRUE.equals(demoData)) {
            initDemoData();
        } else {
            LOG.info("demo data will not be initialized");
        }

    }

    public void initDemoData() {
        LOG.info("systable initialized: " + initSysTable());
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
                            case "order":
                                if (Boolean.FALSE.equals(i.initialized)) {
                                    LOG.info("initializing Orders:");
                                    //           i.initialized = initOrders();
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
        shopSys.add(ShopSys.findByName("order") != null ? ShopSys.findByName("order") : new ShopSys("order", false));
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
            LOG.info(ShopUser.count() + " Benutzer angelegt");
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
                String name = "/TestData/GenreImages/genre" + i.id + ".png";
                PictureHandler pictureHandler = new PictureHandler();
                GenrePicture genrePicture = null;
                try {
                    genrePicture = new GenrePicture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name)));
                } catch (IOException e) {
                    LOG.error(e.toString());
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

    public boolean initArtist() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
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
                Artist artist = new Artist(i.name, artistPicture);
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


    public boolean initArticles() {

        int counter = lazyDemoData ? 150 : 500;

        try (Jsonb jsonb = JsonbBuilder.create()) {
            //put Article Pictures in DB
            IntStream.range(1, counter + 1).forEach(i -> {
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
                articlePicture.persistAndFlush();
                LOG.debug(name);
            });

            LOG.debug(ArticlePicture.count() + " Bilder angelegt");
            //add Articles
            List<Article> articles = jsonb.fromJson(getClass().getResourceAsStream("/TestData/article.json"), new ArrayList<Article>() {
            }.getClass().getGenericSuperclass());
            articles
                    .stream().takeWhile(n -> n.id <= counter)
                    .forEach(i -> {
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

    public boolean initOrders() {
        int counter = lazyDemoData ? 10 : 30;
        int ArticleCount = lazyDemoData ? 150 : 500;
        OrderEndpoint orderEndpoint = new OrderEndpoint();


        IntStream.range(0, counter).forEach(i -> {
            ShopUser shopUser = ShopUser.findById(Integer.toUnsignedLong(new Random().nextInt(29) + 1));
            List<ShoppingCartEntries> cartEntries = new ArrayList<>();
            ShoppingCart shoppingCart = new ShoppingCart();
            SecurityContext securityContext = new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return shopUser.email;
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String s) {
                    return false;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            };

            IntStream.range(1, new Random().nextInt(10)).forEach(n -> {
                ShoppingCartEntries shoppingCartEntries = new ShoppingCartEntries(Article.findById((Integer.toUnsignedLong(new Random().nextInt(ArticleCount) + 1))), (new Random().nextInt(9)) + 1);
                cartEntries.add(shoppingCartEntries);
                LOG.debug(shoppingCartEntries.toString());
            });
            shoppingCart.shoppingCartEntries = cartEntries;
            shoppingCart.paymentMethod = ShopOrder.Payment.VORKASSE;
            shoppingCart.shipping = 15.99;
            LOG.debug(shoppingCart.toString());
            orderEndpoint.createOrder(shoppingCart, securityContext);
        });

        return true;
    }

}
