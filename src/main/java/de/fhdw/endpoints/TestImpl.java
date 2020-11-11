package de.fhdw.endpoints;

import de.fhdw.models.*;
import de.fhdw.util.PictureHandler;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "init", description = "div Test Data and basic Backend Operations for Shop initialization")
@RegisterForReflection
public class TestImpl  {
    private static final Logger LOG = Logger.getLogger(TestImpl.class);

    @GET
    @Transactional
    @Path("userTestData")
    @PermitAll
    @Operation(summary = "generates a few Basic User")
    public List<ShopUser> userTestData() {
        if (ShopUser.findById(1L) == null) {
            ShopUser admin = new ShopUser();
            admin.email = "admin@admin.de";
            admin.title = ShopUser.Title.BENUTZERDEFINIERT;
            admin.firstName = "Daniel";
            admin.lastName = "Pilot";
            admin.password = "Test1234";
            admin.role = ShopUser.Role.ADMIN;
            admin.birth = new Date(773560374);
            admin.street = "amselweg";
            admin.streetNumber = 123;
            admin.town = "Gütersloh";
            admin.postalCode = 33330;
            LOG.debug("Benutzer angelegt: " + admin.toString());
            admin.persist();
        }
        if (ShopUser.findById(2L) == null) {
            ShopUser shopUser = new ShopUser();
            shopUser.title = ShopUser.Title.HERR;
            shopUser.email = "user@user.de";
            shopUser.password = "Test1234";
            shopUser.firstName = "Christoph";
            shopUser.lastName = "Müller";
            shopUser.birth = new Date(873560374);
            shopUser.role = ShopUser.Role.USER;
            shopUser.street = "amselweg";
            shopUser.town = "Paderborn";
            shopUser.streetNumber = 123;
            shopUser.postalCode = 33330;
            shopUser.persist();
            LOG.debug("Benutzer angelegt: " + shopUser.toString());
        }

        return ShopUser.listAll();
    }


    @GET
    @Path("file")
    public String buffer() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/TestData/Images/cat1.jpg");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }

    @GET
    @Path("init")
    @Operation(summary = "inits TestData")
    @Transactional()
    public List<Article> articles() {
        try {
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
                LOG.info(ShopUser.count()+" Benutzer angelegt");

            //put Pictures in DB
            IntStream.range(1, 11).forEach(i -> {
                String name = "/TestData/Images/cat"+i+".jpg";
                try {
                    LOG.debug(i+" "+name);
                    PictureHandler pictureHandler = new PictureHandler();
                    Picture picture = new Picture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name), "jpg"));
                    picture.persist();
                } catch (IOException e) {
                    LOG.error(e.toString());
                    LOG.error(i);
                }
            });
                LOG.info(Picture.count()+" Bilder angelegt");


                //add Genre
                List<Genre> genres = jsonb.fromJson(getClass().getResourceAsStream("/TestData/genre.json"), new ArrayList<Genre>() {
                }.getClass().getGenericSuperclass());
                genres.forEach(i -> {
                    Genre genre = new Genre(i.name);
                    genre.persist();
                    LOG.debug("added Genres: " + genre.toString());
                });
                LOG.info(Genre.count()+" Genres angelegt");

                //add Artists
                List<Artist> artists = jsonb.fromJson(getClass().getResourceAsStream("/TestData/artist.json"), new ArrayList<Artist>() {
                }.getClass().getGenericSuperclass());
                artists.forEach(i -> {
                    Artist artist = new Artist(i.name);
                    artist.persist();
                    LOG.debug("added Genres: " + artist.toString());
                });
                LOG.info(Artist.count()+" Artists angelegt");

                //add Articles
               List<Article> articles = jsonb.fromJson(getClass().getResourceAsStream("/TestData/article.json"), new ArrayList<Article>() {
                }.getClass().getGenericSuperclass());
                articles.forEach(i -> {
                    Article article = new Article();
                    LOG.debug(i.picture);
                    article.picture = i.picture;
                    article.title = i.title;
                    article.genre = i.genre;
                    article.artists = i.artists;
                    article.price = i.price;
                    article.ean = i.ean;
                    article.persist();
                    LOG.debug("added article: " + article.toString());
                });
                LOG.info(Article.count()+" Artikel angelegt");
            }

        }catch (Exception e){
            e.printStackTrace();
            LOG.error(e.toString());
        }



        return Article.listAll();

    }



}
