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

    Response RegisterNewArticleWithPicture(@MultipartForm ArticleForm data) throws IOException;

    Article changeArticle(@MultipartForm ArticleForm data) throws IOException;

    ArticleForm getSIngleArticle(@PathParam long id);

    List<Article> getAllArticlesAsJson();

    List<ArticleForm> getArticleRange(int start, int end);


    Long countAllArticles();

    Response deleteArticle(@PathParam long id);


}
