package de.fhdw.endpoints;

import de.fhdw.forms.GenreDownloadForm;
import de.fhdw.forms.GenreUploadForm;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.MatrixParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;

public interface GenreInterface {
    GenreUploadForm get(@PathParam long id);

    Map<String, GenreDownloadForm> getAll(@MatrixParam("start") boolean start);

    Genre updateGenre(GenreUploadForm data, @Context SecurityContext securityContext);

    Response registerNewGenre(GenreUploadForm data, @Context SecurityContext securityContext);

    Boolean deleteGenre(Genre genre, @Context SecurityContext securityContext);

}
