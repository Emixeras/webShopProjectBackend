package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public interface GenreInterface {
    public Genre get(@PathParam long id) throws Exception;

    public Genre put(Genre genre, @Context SecurityContext securityContext) throws Exception;

    public Genre post(Genre genre, @Context SecurityContext securityContext) throws Exception;

    public Boolean delete(@PathParam long id, @Context SecurityContext securityContext) throws Exception;

}
