package de.fhdw.endpoints;

import de.fhdw.models.Artist;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;


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
    public Artist get(@PathParam long id) {
        Optional<Artist> optional = Artist.findByIdOptional(id);
        Artist artist = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));

        LOG.debug("requested: "+artist.toString());
        return artist;
    }

    @Override
    @GET
    @Operation(summary = "returns all Artists")
    public List<Artist> get() {
        return Artist.listAll();
    }

    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "gets a Single Object identified by id", description = "Returns a MultiPart Object")
    public Artist changeArtist(Artist artist, @Context SecurityContext securityContext) {
        Optional<Artist> optional = Artist.findByIdOptional(artist.id);
        Artist old = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        old.name = artist.name;
        LOG.debug("edited: "+old.toString());
        return old;
    }

    @Override
    @POST
    @RolesAllowed({"employee", "admin"})
    @Operation(summary = "reggister new Artist", description = "only allowed by Admins and Employees")
    public Response registerNewArtist(Artist artist, @Context SecurityContext securityContext) {
        if (artist == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        artist.persist();
        LOG.debug("added: "+artist.toString());
        return Response.accepted().build();
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    @Path("{id}")
    @Operation(summary = "delete Artist identified by ID", description = "only allowed by Admins and Employees")
    public Response deleteArtist(@PathParam long id, @Context SecurityContext securityContext) {
        Optional<Artist> optional = Artist.findByIdOptional(id);
        Artist deletedID = optional.orElseThrow(() -> new WebApplicationException(Response.Status.BAD_REQUEST));

        try {
            deletedID.delete();
            LOG.debug("deleted: "+deletedID.toString());
            return Response.ok().build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
    }
}
