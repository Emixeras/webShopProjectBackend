package de.fhdw.forms;

import de.fhdw.models.Article;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.fhdw.models.Picture;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ArticleForm {

    @FormParam("Article")
    @PartType(MediaType.APPLICATION_JSON)
    public Article article;

    public void setFile(InputStream file) throws IOException {
       this.file = IOUtils.toByteArray(file);
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

    public InputStream getFileAsStream() {
        return new ByteArrayInputStream(file);
    }

    @FormParam("Picture")
    private byte[] file;



}
