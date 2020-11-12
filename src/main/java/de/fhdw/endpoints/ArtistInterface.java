package de.fhdw.endpoints;

import de.fhdw.forms.ArtistForm;
import de.fhdw.models.Artist;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface ArtistInterface {
    ArtistForm get(@PathParam long id);

    List<Artist> get();

    Artist put(@MultipartForm ArtistForm data, @Context SecurityContext securityContext);

    Response post(@MultipartForm ArtistForm data, @Context SecurityContext securityContext);

    Boolean delete(Artist artist, @Context SecurityContext securityContext);
}
