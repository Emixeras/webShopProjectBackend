package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.ArticleMetadata;
import de.fhdw.models.ArticlePicture;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

import javax.ws.rs.core.Response;
import java.io.IOException;

public interface ArticleInterface {
    public String createArticle(@MultipartForm ArticleForm data) throws IOException;
    public Response ChangeArticle();
    public MultipartOutput getArticle();

}
