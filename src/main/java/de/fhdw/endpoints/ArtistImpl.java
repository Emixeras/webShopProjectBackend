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
        Artist a = Artist.findById(id);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return a;
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

        Artist old = Artist.findById(artist.id);
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        old.name = artist.name;
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
        return Response.accepted().build();
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    @Path("{id}")
    @Operation(summary = "delete Artist identified by ID", description = "only allowed by Admins and Employees")
    public Response deleteArtist(@PathParam long id, @Context SecurityContext securityContext) {
        Artist deletedID;
        deletedID = Artist.findById(id);
        if (deletedID == null) {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
        try {
            deletedID.delete();
            return Response.ok().build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
    }
}
