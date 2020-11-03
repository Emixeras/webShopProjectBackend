package de.fhdw.forms;

import de.fhdw.endpoints.ArticleImpl;
import de.fhdw.models.Article;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ArticleForm {
    private static final Logger LOG = Logger.getLogger(ArticleForm.class);

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
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] file;


}
