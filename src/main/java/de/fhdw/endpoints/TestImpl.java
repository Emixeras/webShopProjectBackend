package de.fhdw.endpoints;

import de.fhdw.models.HelloWorld;
import de.fhdw.models.ShopUser;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            admin.password = "Test1234";
            admin.role = ShopUser.Role.ADMIN;
            LOG.info("Benutzer angelegt: " + admin.toString());
            admin.persist();
        }
        if(ShopUser.findById(2L) == null){
            ShopUser shopUser = new ShopUser();
            shopUser.email = "user@user.de";
            shopUser.password = "Test1234";
            shopUser.firstName = "Christoph";
            shopUser.lastName = "Müller";
            shopUser.birth = new Date(873560374);
            shopUser.role = ShopUser.Role.USER;
            shopUser.street = "amselweg";
            shopUser.streetNumber = 123;
            shopUser.postalCode = 33330;
            shopUser.persist();
            LOG.info("Benutzer angelegt: " + shopUser.toString());
        }
        return ShopUser.listAll();
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
    @Path("authenticated")
    public String getAuthenticated(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

}
