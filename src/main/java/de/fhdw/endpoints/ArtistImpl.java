package de.fhdw.endpoints;

import de.fhdw.forms.ArtistDownloadForm;
import de.fhdw.forms.ArtistUploadForm;
import de.fhdw.models.Artist;
import de.fhdw.models.ArtistPicture;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Artist", description = "Operations on Artist object")

@Path("artist")
public class ArtistImpl implements ArtistInterface {
    private static final Logger LOG = Logger.getLogger(ArtistImpl.class);


    @Override
    @GET
    @Path("{id}")
    @Operation(summary = "gets a Single  Artist Object identified by id")
    public ArtistDownloadForm get(@PathParam long id) {
        Optional<Artist> optional = Artist.findByIdOptional(id);
        Artist artist = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));
        LOG.debug("requested: " + artist.toString());
        return new ArtistDownloadForm(artist, Base64.getEncoder().encodeToString(artist.picture.rawData));
    }

    @Override
    @GET
    @Path("range")
    public List<ArtistDownloadForm> getArtistRange(@MatrixParam("start") int start, @MatrixParam("end") int end) {
        PanacheQuery<Artist> panacheQuery = Artist.findAll(Sort.by("id"));

        return panacheQuery
                .range(start - 1, end - 1)
                .list()
                .stream().map(
                        artist -> {
                            ArtistDownloadForm artistDownloadForm = new ArtistDownloadForm();
                            artistDownloadForm.artist = artist;
                            artistDownloadForm.file = Base64.getEncoder().encodeToString(artist.picture.rawData);
                            return artistDownloadForm;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    @GET
    @Operation(summary = "returns all Artists")
    public List<Artist> get() {
        return Artist.findAll(Sort.by("name")).list();
    }

    @Override
    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "updates a Artist Object")
    public Response changeArtist(@MultipartForm ArtistUploadForm data, @Context SecurityContext securityContext) {
        Optional<Artist> optional = Artist.findByIdOptional(data.artist.id);
        Artist old = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        old.name = data.artist.name;

        if (data.getFile() == null) {
            old.picture.rawData = data.getFile();
        }

        LOG.debug("edited: " + old.toString());
        ArtistDownloadForm artistDownloadForm = new ArtistDownloadForm();
        artistDownloadForm.artist = old;
        artistDownloadForm.file = Base64.getEncoder().encodeToString(old.picture.rawData);

        return Response.ok().entity(artistDownloadForm).build();
    }


    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    @Transactional
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "reggister new Artist", description = "only allowed by Admins and Employees")
    public Response registerNewArtist(@MultipartForm ArtistUploadForm data, @Context SecurityContext securityContext) {
        if (data.artist == null || data.getFile() == null)
            return Response.noContent().build();

        if (Artist.findByName(data.artist.name) != null)
            return Response
                    .status(409)
                    .entity(Artist.findByName(data.artist.name))
                    .build();

        if (data.getFile() == null) {
            return Response
                    .status(409)
                    .entity(new RestError(data.artist, "picture is missing"))
                    .build();
        }

        try {
            ArtistPicture artistPicture = new ArtistPicture(data.getFile());
            artistPicture.persist();
            Artist artist = new Artist(data.artist.name, artistPicture);
            artist.persist();
            LOG.debug("added: " + artist.toString());
        } catch (Exception e) {
            LOG.info(e.toString());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return Response.accepted(data.artist).build();
    }

    @Override
    @DELETE
    @Transactional
    @RolesAllowed({"employee", "admin"})
    @Path("{id}")
    @Operation(summary = "delete Artist identified by ID", description = "only allowed by Admins and Employees")
    public Response deleteArtist(@PathParam long id, @Context SecurityContext securityContext) {
        Optional<Artist> optional = Artist.findByIdOptional(id);
        Artist deletedID = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));

        try {
            deletedID.delete();
            LOG.debug("deleted: " + deletedID.toString());
            return Response.ok().build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
    }

    private static class RestError {
        public RestError(Artist artist, String message) {
        }
    }
}
