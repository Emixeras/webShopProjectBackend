package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.Article;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public interface ArticleInterface {

    public Response post(@MultipartForm ArticleForm data) throws IOException;

    public Response put(@MultipartForm ArticleForm data) throws IOException;

    public ArticleForm getSingle(@PathParam long id);

    public List<Article> getAll();

    public List<ArticleForm> getRange(int start, int end);

    public List<ArticleForm> getByGenre(int start, int end);

    public List<ArticleForm> getByArtist(int start, int end);

    public Long getCount();

    public Response delete(@PathParam long id);




}
