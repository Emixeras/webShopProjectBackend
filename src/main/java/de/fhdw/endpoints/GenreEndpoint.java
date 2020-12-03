package de.fhdw.endpoints;

import de.fhdw.forms.GenreDownloadForm;
import de.fhdw.forms.GenreUploadForm;
import de.fhdw.models.Genre;
import de.fhdw.models.GenrePicture;
import de.fhdw.util.PictureHandler;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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
public class GenreEndpoint  {
    private static final Logger LOG = Logger.getLogger(GenreEndpoint.class);

    @Inject
    PictureHandler pictureHandler;

    @GET
    @Path("{id}")
    @Operation(summary = "gets a Single Object identified by id", description = "Returns a MultiPart Object")
    public GenreDownloadForm get(@org.jboss.resteasy.annotations.jaxrs.PathParam long id) {
        GenreDownloadForm genreDownloadForm = new GenreDownloadForm();
        Genre genre = Genre.findById(id);
        if (genre == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        genreDownloadForm.genre = genre;
        genreDownloadForm.file = Base64.getEncoder().encodeToString(genre.picture.rawData);
        return genreDownloadForm;
    }

    @GET
    @Operation(summary = "returns all Genres with Picture", description = "returns Genres with or without Pictures Example: http://localhost:8080/genre;picture=true")
    public Map<String, GenreDownloadForm> getAll(@MatrixParam("picture") boolean picture) {
        Map<String, GenreDownloadForm> map = new HashMap<>();
        List<Genre> genres = Genre.listAll();
        if (picture) {
            for (Genre genre : genres) {
                GenreDownloadForm genreDownloadForm = new GenreDownloadForm();
                genreDownloadForm.file = Base64.getEncoder().encodeToString(genre.picture.rawData);
                genreDownloadForm.genre = genre;
                map.put(genre.id.toString(), genreDownloadForm);
            }
        } else {
            for (Genre genre : genres) {
                GenreDownloadForm genreDownloadForm = new GenreDownloadForm();
                genreDownloadForm.genre = genre;
                map.put(genre.id.toString(), genreDownloadForm);
            }
        }
        return map;
    }

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
            old.picture.rawData = data.getFile();
            old.picture.thumbnail = pictureHandler.scaleImage(data.getFileAsStream());
        } catch (Exception e) {
            LOG.error(e.toString());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return old;
    }

    @POST
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "creates a new Genre Object", description = "Accepts a Multipart Object")
    public Response registerNewGenre(@MultipartForm GenreUploadForm data, @Context SecurityContext securityContext) {
        if (data.genre == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        try {
            GenrePicture genrePicture = new GenrePicture(data.getFile(), pictureHandler.scaleImage(data.getFileAsStream()));
            genrePicture.persist();
            data.genre.picture = genrePicture;
            data.genre.persist();
            LOG.info("added: " + data.genre.toString());
            return Response.accepted(data.genre.id).build();
        } catch (Exception e) {
            LOG.error(e.toString());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }


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
