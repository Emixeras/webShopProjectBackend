package de.fhdw.endpoints;

import de.fhdw.forms.GenreForm;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface GenreInterface {
    public Genre get(@PathParam long id) ;

    public List<Genre> get();

    public Response put(GenreForm data, @Context SecurityContext securityContext) ;

    public Response post(GenreForm data, @Context SecurityContext securityContext);

    public Boolean delete(Genre genre, @Context SecurityContext securityContext);

}
