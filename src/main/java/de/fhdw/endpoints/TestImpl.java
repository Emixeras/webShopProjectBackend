package de.fhdw.endpoints;

import de.fhdw.models.*;
import de.fhdw.util.PictureHandler;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Test", description = "div Test Data and basic Backend Operations")
@RegisterForReflection
public class TestImpl  {
    private static final Logger LOG = Logger.getLogger(TestImpl.class);
    private static final String DATEPATTERN = "yyyy-MM-dd HH:mm:ss";

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
            LOG.info("Benutzer angelegt: " + admin.toString());
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
            LOG.info("Benutzer angelegt: " + shopUser.toString());
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
    public List<Article> Articles() throws IOException {

        //put Pictures in DB
        IntStream.range(1, 11).forEach(i -> {
            String name = "/TestData/Images/cat"+i+".jpg";
            try {
                LOG.debug(i+" "+name);
                PictureHandler pictureHandler = new PictureHandler();
                Picture picture = new Picture(IOUtils.toByteArray(getClass().getResourceAsStream(name)), pictureHandler.scaleImage(getClass().getResourceAsStream(name), pictureHandler.checkImageFormat(getClass().getResourceAsStream(name))));
                picture.persist();
            } catch (IOException e) {
                LOG.error(e.toString());
                LOG.error(i);
            }
        });
        Jsonb jsonb = JsonbBuilder.create();
        List<Genre> genres = jsonb.fromJson(getClass().getResourceAsStream("/TestData/genre.json"), new ArrayList<Genre>() {
        }.getClass().getGenericSuperclass());
        genres.forEach(i -> {
            Genre genre = new Genre(i.name);
            genre.persist();
            LOG.debug("added Genres: " + genre.toString());
        });

        List<Artist> artists = jsonb.fromJson(getClass().getResourceAsStream("/TestData/artist.json"), new ArrayList<Artist>() {
        }.getClass().getGenericSuperclass());
        artists.forEach(i -> {
            Artist artist = new Artist(i.name);
            artist.persist();
            LOG.debug("added Genres: " + artist.toString());
        });

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


        return Article.listAll();

    }



}
