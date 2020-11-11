package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.Article;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArticleInterface {

    Response post(@MultipartForm ArticleForm data) throws IOException;

    Response put(@MultipartForm ArticleForm data) throws IOException;

    ArticleForm getSingle(@PathParam long id);

    List<Article> getAll();

    List<ArticleForm> getRange(int start, int end);


    Long getCount();

    Response delete(@PathParam long id);


}
