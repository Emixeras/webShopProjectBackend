package de.fhdw.forms;

import de.fhdw.models.ArticleMetadata;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

import de.fhdw.models.ArticlePicture;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ArticleForm {

    @FormParam("picture")
    @PartType(MediaType.APPLICATION_JSON)
    public ArticleMetadata Article;

    @FormParam("articleMetadata")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public Byte[] picture;
}
