package de.fhdw.endpoints;

import de.fhdw.models.*;
import org.jboss.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@ApplicationScoped
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestImpl implements TestInterface {
    private static final Logger LOG = Logger.getLogger(TestImpl.class);
    private static final String DATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    @GET
    @Transactional
    @Path("userTestData")
    @PermitAll
    public List<ShopUser> userTestData() {
        if (ShopUser.findById(1L) == null) {
            ShopUser admin = new ShopUser();
            admin.email = "admin@admin.de";
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
    public List<Article> articleTestData() {
        Genre genre = new Genre("rock");
        Artist artist = new Artist("Die Ärzte");
        if (Artist.findByName("Die Ärzte") == null) {
            artist.persist();
        }
        if(Genre.findByName("rock")==null){
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
            LOG.info("added: "+art1.toString());
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
            LOG.info("added: "+art2.toString());

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
            LOG.info("added: "+art3.toString());

        }
        return Article.listAll();
    }

    @Override
    @GET
    public HelloWorld get() {
        LOG.info("Dies ist der Hello World Test um "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
        return new HelloWorld("Dies ist der Hello World Test um "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
    }

    @POST
    @Override
    public HelloWorld post(HelloWorld helloWorld) {
        LOG.info(helloWorld.value + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
        return helloWorld;
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("admin")
    public String getAdmin(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("employee")
    public String getEmployee(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("user")
    public String getUser(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

}
