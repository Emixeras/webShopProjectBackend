package de.fhdw.endpoints;

import de.fhdw.models.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Test", description = "div Test Data and basic Backend Operations")

public class TestImpl implements TestInterface {
    private static final Logger LOG = Logger.getLogger(TestImpl.class);
    private static final String DATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
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

    @Override
    @GET
    @Transactional
    @Path("articleTestData")
    @Operation(summary = "generates Article Test Data")
    public List<Article> articleTestData() {
        Genre genre = new Genre("rock");
        Artist artist = new Artist("Die Ärzte");
        if (Artist.findByName("Die Ärzte") == null) {
            artist.persist();
        }
        if (Genre.findByName("rock") == null) {
            genre.persist();
        }
        if (Article.findById(1L) == null) {
            Article art1 = new Article();
            art1.description = "blub";
            art1.ean = 123;
            art1.price = 12.99;
            art1.title = "desc1";
            art1.artists = Artist.findByName("Die Ärzte");
            art1.genre = Genre.findByName("rock");
            art1.persist();
            LOG.info("added: " + art1.toString());
        }
        if (Article.findById(2L) == null) {
            Article art2 = new Article();
            art2.artists = Artist.findByName("Die Ärzte");
            art2.description = "blub";
            art2.ean = 123;
            art2.price = 12.99;
            art2.title = "desc2";
            art2.genre = Genre.findByName("rock");
            art2.persist();
            LOG.info("added: " + art2.toString());

        }
        if (Article.findById(3L) == null) {
            Article art3 = new Article();
            art3.artists = Artist.findByName("Die Ärzte");
            art3.description = "blub";
            art3.ean = 123;
            art3.price = 12.99;
            art3.title = "desc3";
            art3.genre = Genre.findByName("rock");
            art3.persist();
            LOG.info("added: " + art3.toString());

        }
        return Article.listAll();
    }

    @Override
    @GET
    @Operation(summary = "Basic Hello World Object")
    public HelloWorld get() {
        LOG.info("Dies ist der Hello World Test um "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
        return new HelloWorld("Dies ist der Hello World Test um "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
    }

    @POST
    @Override
    @Operation(summary = "Basic Hello World Object printed in BackendLog")
    public HelloWorld post(HelloWorld helloWorld) {
        LOG.info(helloWorld.value + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
        return helloWorld;
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("admin")
    @Operation(summary = "Basic Endpoint Accepts an Admin User")
    public String getAdmin(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("employee")
    @Operation(summary = "Basic Endpoint Accepts an Employee User")
    public String getEmployee(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("user")
    @Operation(summary = "Basic Endpoint Accepts a User")
    public String getUser(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

}
