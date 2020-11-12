package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface ArtistInterface {
    Artist get(@PathParam long id);

    List<Artist> get();

    Artist put(Artist artist, @Context SecurityContext securityContext);

    Response post(Artist artist, @Context SecurityContext securityContext);

    Response delete( long id, @Context SecurityContext securityContext);
}
