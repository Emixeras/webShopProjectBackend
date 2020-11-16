package de.fhdw.endpoints;

import de.fhdw.forms.ArtistDownloadForm;
import de.fhdw.models.Artist;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public interface ArtistInterface {
    ArtistDownloadForm get(@PathParam long id);

    List<ArtistDownloadForm> getArtistRange(int start, int end);

    List<Artist> get();

    Artist changeArtist(Artist artist, @Context SecurityContext securityContext);

    Response registerNewArtist(Artist artist, @Context SecurityContext securityContext);

    Response deleteArtist(long id, @Context SecurityContext securityContext);
}
