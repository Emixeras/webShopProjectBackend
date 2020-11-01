package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.Article;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public interface ArticleInterface {

    public Response post(@MultipartForm ArticleForm data) throws IOException;

    public Response put();

    public MultipartOutput get(@PathParam long id);

    public List<Article> get();

    public Response delete(@PathParam long id);




}
