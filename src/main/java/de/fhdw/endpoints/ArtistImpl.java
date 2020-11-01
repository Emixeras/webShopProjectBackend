package de.fhdw.endpoints;

import de.fhdw.models.Artist;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.wildfly.common.annotation.NotNull;

import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("artist")
public class ArtistImpl implements ArtistInterface {

    @Override
    @GET
    @Path("{id}")
    public Artist get(@PathParam long id) throws Exception {
        try {
            return Artist.findById(id);
        } catch (Exception e) {
            throw new Exception("id not found");
        }
    }

    @Override
    public List<Artist> get() {
        return Artist.listAll();
    }


    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    public Artist put(Artist artist, @Context SecurityContext securityContext) throws Exception {
        try {
            Artist old = Artist.findById(artist.id);
            old.name = artist.name;
            return old;
        } catch (Exception E) {
            throw new Exception("Fehlgeschlagen");
        }
    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    public Artist post(@NotNull Artist artist, @Context SecurityContext securityContext) throws Exception {
        try {
            artist.persist();
            return artist;
        } catch (Exception e) {
            throw new Exception("could not persist user");
        }
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    public Boolean delete(Artist artist, @Context SecurityContext securityContext) throws Exception {
        Artist deletedID;
        try {
            deletedID = Artist.findById(artist.id);
            deletedID.delete();
            return true;
        } catch (Exception e) {
            throw new Exception("Artist does not exist");
        }
    }
}
