package de.fhdw.endpoints;

import de.fhdw.forms.ArticleDownloadForm;
import de.fhdw.forms.ArtistDownloadForm;
import de.fhdw.forms.ArtistUploadForm;
import de.fhdw.models.Article;
import de.fhdw.models.Artist;
import de.fhdw.models.ArtistPicture;
import de.fhdw.util.RestError;
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
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Artist", description = "Operations on Artist object")

@Path("artist")
public class ArtistEndpoint {
    private static final Logger LOG = Logger.getLogger(ArtistEndpoint.class);


    @GET
    @Path("{id}")
    @Operation(summary = "gets a Single  Artist Object identified by id")
    public ArtistDownloadForm get(@PathParam long id) {
        Optional<Artist> optional = Artist.findByIdOptional(id);
        Artist artist = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));
        LOG.debug("requested: " + artist.toString());
        return new ArtistDownloadForm(artist, Base64.getEncoder().encodeToString(artist.picture.rawData));
    }

    @GET
    @Path("range")
    public Response getArtistRange(@MatrixParam("start") int start, @MatrixParam("end") int end) {
        PanacheQuery<Artist> panacheQuery;

        if (start == end) {
            ArtistDownloadForm artistDownloadForm = new ArtistDownloadForm();
            Artist artist = Artist.findById(Integer.toUnsignedLong(start));
            artistDownloadForm.artist = artist;
            try {
                artistDownloadForm.file = Base64.getEncoder().encodeToString(artist.picture.rawData);
            } catch (Exception e) {
                LOG.error("input Failed");
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            return Response.ok().entity(artistDownloadForm).build();
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("sID", Integer.toUnsignedLong(start));
            params.put("eID", Integer.toUnsignedLong(end));
            panacheQuery = Artist.find("#Artist.getRange", params);
        }

        return Response.ok().entity(panacheQuery
                .list()
                .stream()
                .map(
                        artist -> {
                            ArtistDownloadForm artistDownloadForm = new ArtistDownloadForm();
                            artistDownloadForm.artist = artist;
                            artistDownloadForm.file = Base64.getEncoder().encodeToString(artist.picture.rawData);
                            return artistDownloadForm;
                        }
                ).collect(Collectors.toList())).build();
    }

    @GET
    @Operation(summary = "returns all Artists")
    public List<Artist> get() {
        return Artist.findAll(Sort.by("name")).list();
    }

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


}
