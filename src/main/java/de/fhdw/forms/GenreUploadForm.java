package de.fhdw.forms;

import de.fhdw.models.Genre;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GenreUploadForm {

    @FormParam("Genre")
    @PartType(MediaType.APPLICATION_JSON)
    public Genre genre;

    @FormParam("Picture")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] file;

    public byte[] getFile() {
        return file;
    }

    public void setFile(InputStream file) throws IOException {
        this.file = IOUtils.toByteArray(file);
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public InputStream getFileAsStream() {
        return new ByteArrayInputStream(file);
    }

}
