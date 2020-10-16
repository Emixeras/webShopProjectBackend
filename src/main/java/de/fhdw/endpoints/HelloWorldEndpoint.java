package de.fhdw.endpoints;

import de.fhdw.models.HelloWorld;
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


    @Path("get")
    @GET
    public de.fhdw.models.HelloWorld sendHelloWorld() {
        System.out.println("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new de.fhdw.models.HelloWorld("Dies ist der Hello World Test um " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Path("send")
    @POST
    public de.fhdw.models.HelloWorld getHelloWorld(de.fhdw.models.HelloWorld helloWorld) {
        System.out.println(helloWorld.value + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return helloWorld;
    }


}
