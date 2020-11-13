package de.fhdw.endpoints;

import de.fhdw.forms.ArticleDownloadForm;
import de.fhdw.forms.ArticleUploadForm;
import de.fhdw.models.Article;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public interface ArticleInterface {

    Response RegisterNewArticleWithPicture(@MultipartForm ArticleUploadForm data) throws IOException;

    Article changeArticle(@MultipartForm ArticleUploadForm data) throws IOException;

    ArticleDownloadForm getSingleArticle(@PathParam long id);

    List<Article> getAllArticlesAsJson();

    List<ArticleDownloadForm> getArticleRange(int start, int end);

    Long countAllArticles();

    Response deleteArticle(@PathParam long id);


}
