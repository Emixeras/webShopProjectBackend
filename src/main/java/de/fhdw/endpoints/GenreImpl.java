package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Cacheable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("genre")
public class GenreImpl implements GenreInterface {
    @Override
    @GET
    @Path("{id}")
    public Genre get(long id) {

        Genre genre = Genre.findById(id);
        if (genre == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Genre.findById(id);
    }

    @Override
    @Cache
    public List<Genre> get() {
        return Genre.listAll();
    }

    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    public Genre put(Genre genre, SecurityContext securityContext) {
        Genre old = Artist.findById(genre.id);
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        old.name = genre.name;
        return old;
    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    public Genre post(Genre genre, SecurityContext securityContext) {
        try {
            genre.persist();
            return genre;
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    public Boolean delete(Genre genre, SecurityContext securityContext)  {
        Genre deletedID = Genre.findById(genre.id);
        if(deletedID == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        try {
            deletedID.delete();
            return true;
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
    }
}
