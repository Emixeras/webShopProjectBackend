package de.fhdw.endpoints;

import de.fhdw.forms.GenreForm;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Map;

public interface GenreInterface {
    GenreForm get(@PathParam long id);

    Map<String, GenreForm> getAll();

    Genre updateGenre(GenreForm data, @Context SecurityContext securityContext);

    Response registerNewGenre(GenreForm data, @Context SecurityContext securityContext);

    Boolean deleteGenre(Genre genre, @Context SecurityContext securityContext);

}
