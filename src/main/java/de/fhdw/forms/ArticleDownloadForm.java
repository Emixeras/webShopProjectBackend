package de.fhdw.forms;

import de.fhdw.models.Article;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

public class ArticleDownloadForm {

    @FormParam("Genre")
    @PartType(MediaType.APPLICATION_JSON)
    public Article article;

    @FormParam("Picture")
    @PartType(MediaType.TEXT_PLAIN)
    public String file;

}
