package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface ArtistInterface {
    public Artist get(@PathParam long id) throws Exception;

    public List<Artist> get();

    public Artist put(Artist artist, @Context SecurityContext securityContext) throws Exception;

    public Artist post(Artist artist, @Context SecurityContext securityContext) throws Exception;

    public Boolean delete(Artist artist, @Context SecurityContext securityContext) throws Exception;
}
