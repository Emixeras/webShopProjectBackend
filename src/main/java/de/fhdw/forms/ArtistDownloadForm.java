package de.fhdw.forms;

import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

public class ArtistDownloadForm {

    @FormParam("Artist")
    @PartType(MediaType.APPLICATION_JSON)
    public Artist artist;

    @FormParam("Picture")
    @PartType(MediaType.TEXT_PLAIN)
    public String file;


    public ArtistDownloadForm() {
    }

    public ArtistDownloadForm(Artist artist, String file) {
        this.artist = artist;
        this.file = file;
    }
}
