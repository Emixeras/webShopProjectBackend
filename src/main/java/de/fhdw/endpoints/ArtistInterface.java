package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public interface ArtistInterface {
    public Artist get(@PathParam long id) throws Exception;

    public Artist put(Artist artist, @Context SecurityContext securityContext) throws Exception;

    public Artist post(Artist artist, @Context SecurityContext securityContext) throws Exception;

    public Boolean delete(@PathParam long id, @Context SecurityContext securityContext) throws Exception;
}
