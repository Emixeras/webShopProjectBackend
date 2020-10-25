package de.fhdw.endpoints;

import de.fhdw.models.HelloWorld;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
@Path("/api/helloWorld")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloWorldEndpoint {

    private static final Logger LOG = Logger.getLogger(HelloWorldEndpoint.class);

    @Path("get")
    @GET
    public HelloWorld get() {
        LOG.info("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new HelloWorld("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Path("post")
    @POST
    public HelloWorld post(HelloWorld helloWorld) {
        LOG.info(helloWorld.value + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return helloWorld;
    }

    @Path("admin")
    @GET
    @RolesAllowed("admin")
    public String getAuthenticated(@Context SecurityContext securityContext){
        return securityContext.getUserPrincipal().getName();
    }

}
