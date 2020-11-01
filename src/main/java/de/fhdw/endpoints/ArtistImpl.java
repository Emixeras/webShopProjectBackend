package de.fhdw.endpoints;

import de.fhdw.models.Artist;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Artist get(@PathParam long id) {
        Artist a = Artist.findById(id);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Artist.findById(id);
    }

    @Override
    public List<Artist> get() {
        return Artist.listAll();
    }


    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    public Artist put(Artist artist, @Context SecurityContext securityContext)  {
            Artist old = Artist.findById(artist.id);
            if(old == null){
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            old.name = artist.name;
            return old;
    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    public Artist post(@NotNull Artist artist, @Context SecurityContext securityContext)  {
        try {
            artist.persist();
            return artist;
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    public Boolean delete(Artist artist, @Context SecurityContext securityContext)  {
        Artist deletedID;
        deletedID = Artist.findById(artist.id);
        if(deletedID == null){
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
        try {
            deletedID.delete();
            return true;
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
    }
}
