package de.fhdw.endpoints;

import de.fhdw.forms.GenreForm;
import de.fhdw.models.Genre;
import de.fhdw.models.Picture;
import de.fhdw.util.PictureHandler;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.Cache;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("genre")
@Tag(name = "Genre", description = "Operations on Genre object")
public class GenreImpl implements GenreInterface {
    private static final Logger LOG = Logger.getLogger(GenreImpl.class);

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
    @Operation(summary = "put2", description = "put2")
    public Response put(@MultipartForm GenreForm data, @Context SecurityContext securityContext) {
        Genre old = Genre.findById(data.genre.id);
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        PictureHandler pictureHandler = new PictureHandler();
        String media;
        try {
            media = pictureHandler.checkImageFormat(data.getFileAsStream());
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (media.equals("image/png") || media.equals("image/jpeg")) {
            old.image.data = data.getFile();
        }
        return Response.ok().build();

    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    public Response post(@MultipartForm GenreForm data, @Context SecurityContext securityContext) {

        if (data.genre == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        PictureHandler pictureHandler = new PictureHandler();
        String media;
        try {
            media = pictureHandler.checkImageFormat(data.getFileAsStream());
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (media.equals("image/png") || media.equals("image/jpeg")) {
            Genre genre = data.genre;
            Picture picture = new Picture(data.getFile());
            picture.persist();
            genre.image = picture;
            genre.persist();
            LOG.info("added: " + genre.toString());
            return Response.accepted(data.genre.id).build();
        }
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    public Boolean delete(Genre genre, SecurityContext securityContext) {
        Genre deletedID = Genre.findById(genre.id);
        if (deletedID == null) {
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
