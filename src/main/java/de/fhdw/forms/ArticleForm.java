package de.fhdw.forms;

import de.fhdw.models.Article;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ArticleForm {

    @FormParam("ArticleMetadata")
    @PartType(MediaType.APPLICATION_JSON)
    public Article article;

    @FormParam("Picture")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;


}
