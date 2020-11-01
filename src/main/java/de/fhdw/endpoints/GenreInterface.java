package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface GenreInterface {
    public Genre get(@PathParam long id) throws Exception;

    public List<Genre> get() throws Exception;

    public Genre put(Genre genre, @Context SecurityContext securityContext) ;

    public Genre post(Genre genre, @Context SecurityContext securityContext);

    public Boolean delete(Genre genre, @Context SecurityContext securityContext);

}
