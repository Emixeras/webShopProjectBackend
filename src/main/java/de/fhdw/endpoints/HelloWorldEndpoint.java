package de.fhdw.endpoints;

import de.fhdw.models.HelloWorld;
import org.hibernate.annotations.common.util.impl.Log;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@ApplicationScoped
@Path("/api/HelloWorld")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloWorldEndpoint {

    private static final Logger LOG = Logger.getLogger(HelloWorldEndpoint.class);

    @Path("get")
    @GET
    public de.fhdw.models.HelloWorld sendHelloWorld() {
        LOG.info("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new de.fhdw.models.HelloWorld("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Path("send")
    @POST
    public de.fhdw.models.HelloWorld getHelloWorld(de.fhdw.models.HelloWorld helloWorld) {
        LOG.info(helloWorld.value + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return helloWorld;
    }


}
