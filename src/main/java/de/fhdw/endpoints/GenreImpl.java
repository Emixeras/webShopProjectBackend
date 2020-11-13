package de.fhdw.endpoints;

import de.fhdw.forms.GenreDownloadForm;
import de.fhdw.forms.GenreUploadForm;
import de.fhdw.models.Genre;
import de.fhdw.models.Picture;
import de.fhdw.util.PictureHandler;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public GenreUploadForm get(long id) {
        GenreUploadForm genreUploadForm = new GenreUploadForm();
        Genre genre = Genre.findById(id);
        if (genre == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        genreUploadForm.genre = genre;
        genreUploadForm.setFile(genre.picture.rawData);
        return genreUploadForm;
    }

    @GET
    @Override
    @Operation(summary = "returns all Genres with Picture")
    public Map<String, GenreDownloadForm> getAll() {
        Map<String, GenreDownloadForm> map = new HashMap<>();
        List<Genre> genres = Genre.listAll();
        for (Genre genre : genres) {
            GenreDownloadForm genreDownloadForm = new GenreDownloadForm();
            genreDownloadForm.file = Base64.getEncoder().encodeToString(genre.picture.rawData);
            genreDownloadForm.genre = genre;
            map.put(genre.id.toString(), genreDownloadForm);
        }
        return map;
    }

    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "modifes a Genre Object", description = "Accepts a Multipart Object")
    public Genre updateGenre(@MultipartForm GenreUploadForm data, @Context SecurityContext securityContext) {
        Genre old = Genre.findById(data.genre.id);
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        try {
            old.name = data.genre.name;
            PictureHandler pictureHandler = new PictureHandler();
            old.picture.rawData = data.getFile();
            old.picture.thumbnail = pictureHandler.scaleImage(data.getFileAsStream());
        } catch (Exception e) {
            LOG.error(e.toString());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return old;
    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "creates a new Genre Object", description = "Accepts a Multipart Object")
    public Response registerNewGenre(@MultipartForm GenreUploadForm data, @Context SecurityContext securityContext) {
        if (data.genre == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            PictureHandler pictureHandler = new PictureHandler();
            Picture picture = new Picture(data.getFile(), pictureHandler.scaleImage(data.getFileAsStream()));
            picture.persist();
            data.genre.picture = picture;
            data.genre.persist();
            LOG.info("added: " + data.genre.toString());
            return Response.accepted(data.genre.id).build();
        } catch (Exception e) {
            LOG.error(e.toString());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "deletes a Genre Object", description = "Deletes an object identified by id")
    public Boolean deleteGenre(Genre genre, SecurityContext securityContext) {
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
