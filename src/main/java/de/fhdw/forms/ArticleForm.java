package de.fhdw.forms;

import de.fhdw.endpoints.TestImpl;
import de.fhdw.models.ArticleMetadata;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

import de.fhdw.models.ArticlePicture;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class ArticleForm {

    @FormParam("ArticleMetadata")
    @PartType(MediaType.APPLICATION_JSON)
    public ArticleMetadata article;

    @FormParam("ArticlePicture")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream file;
}
