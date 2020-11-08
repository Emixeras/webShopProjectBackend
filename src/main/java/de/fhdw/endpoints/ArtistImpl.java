package de.fhdw.endpoints;
import de.fhdw.forms.ArtistForm;
import de.fhdw.models.Artist;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import de.fhdw.models.Picture;
import de.fhdw.util.PictureHandler;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("artist")
public class ArtistImpl implements ArtistInterface {
    private static final Logger LOG = Logger.getLogger(ArtistImpl.class);


    @Override
    @GET
    @Path("{id}")
    public ArtistForm get(@PathParam long id) {
        Artist a = Artist.findById(id);
        if (a == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        ArtistForm artistForm = new ArtistForm();
        artistForm.artist = a;
        artistForm.setFile(a.image.data);
        return artistForm;
    }

    @Override
    public List<Artist> get() {
        return Artist.listAll();
    }


    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    public Artist put(Artist data, @Context SecurityContext securityContext) {
        Artist old = Artist.findById(data.id);
        if (old == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        old.name = data.name;
        return old;
    }

    @Override
    @PUT
    @RolesAllowed({"employee", "admin"})
    public Response put(@MultipartForm ArtistForm data, @Context SecurityContext securityContext) {
        Artist old = Artist.findById(data.artist.id);
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
    public Response post(@MultipartForm ArtistForm data, @Context SecurityContext securityContext) {

        if (data.artist == null) {
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
            Artist artist = data.artist;
            Picture picture = new Picture(data.getFile());
            picture.persist();
            artist.image = picture;
            artist.persist();
            LOG.info("added: " + artist.toString());
            return Response.accepted(data.artist.id).build();
        }
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    @DELETE
    @RolesAllowed({"employee", "admin"})
    public Boolean delete(Artist artist, @Context SecurityContext securityContext) {
        Artist deletedID;
        deletedID = Artist.findById(artist.id);
        if (deletedID == null) {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
        try {
            deletedID.delete();
            return true;
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
    }
}
