package de.fhdw.forms;

import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

public class GenreDownloadForm {

    @FormParam("Genre")
    @PartType(MediaType.APPLICATION_JSON)
    public Genre genre;

    @FormParam("Picture")
    @PartType(MediaType.TEXT_PLAIN)
    public String file;


}
