package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import de.fhdw.models.Genre;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("genre")
public class GenreImpl implements GenreInterface {
    @Override
    @GET
    @Path("{id}")
    public Genre get(long id) throws Exception {
        try {
            return Genre.findById(id);
        } catch (Exception e) {
            throw new Exception("id not found");
        }
    }

    @Override
    public List<Genre> get() throws Exception {
        return Genre.listAll();
    }

    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    public Genre put(Genre genre, SecurityContext securityContext) throws Exception {
        try {
            Genre old = Artist.findById(genre.id);
            old.name = genre.name;
            return old;
        } catch (Exception E) {
            throw new Exception("Fehlgeschlagen");
        }
    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    public Genre post(Genre genre, SecurityContext securityContext) throws Exception {
        try {
            genre.persist();
            return genre;
        } catch (Exception e) {
            throw new Exception("could not persist user");
        }
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    public Boolean delete(Genre genre, SecurityContext securityContext) throws Exception {
        Genre deletedID;
        try {
            deletedID = Genre.findById(genre.id);
            deletedID.delete();
            return true;
        } catch (Exception e) {
            throw new Exception("User does not exist");
        }
    }
}
