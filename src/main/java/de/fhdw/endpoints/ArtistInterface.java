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
    public ArtistForm get(@PathParam long id);

    public List<Artist> get();

    public Response put(@MultipartForm ArtistForm data, @Context SecurityContext securityContext);
    public Artist put(Artist artist, @Context SecurityContext securityContext);

    public Response post(@MultipartForm ArtistForm data, @Context SecurityContext securityContext);

    public Boolean delete(Artist artist, @Context SecurityContext securityContext);
}
