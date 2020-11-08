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
    @Operation(summary = "gets a Single Object identified by id", description = "Returns a MultiPart Object")
    public GenreForm get(long id) {
        GenreForm genreForm = new GenreForm();
        Genre genre = Genre.findById(id);
        if (genre == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        genreForm.genre = genre;
        genreForm.setFile(genre.image.rawData);
        return genreForm;
    }

    @Override
    @Cache
    public List<Genre> get() {
        return Genre.listAll();
    }

    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "modifes a Genre Object", description = "Accepts a Multipart Object")
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
            old.image.rawData = data.getFile();
        }
        return Response.ok().build();

    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "creates a new Genre Object", description = "Accepts a Multipart Object")
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
           try {
               Genre genre = data.genre;
               String type = media.equals("image/jpeg") ? "jpg":"png";
               Picture picture = new Picture(data.getFile(), pictureHandler.scaleAbleImage(data.getFileAsStream(), type));
               picture.persist();
               genre.image = picture;
               genre.persist();
               LOG.info("added: " + genre.toString());
               return Response.accepted(data.genre.id).build();
           }catch (Exception e){
               LOG.info(e.toString());
               throw new WebApplicationException(Response.Status.NOT_FOUND);
           }
        }
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "deletes a Genre Object", description = "Deletes an object identified by id")
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
