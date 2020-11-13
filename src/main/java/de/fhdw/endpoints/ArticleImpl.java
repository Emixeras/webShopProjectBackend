package de.fhdw.endpoints;

import de.fhdw.forms.ArticleDownloadForm;
import de.fhdw.forms.ArticleUploadForm;
import de.fhdw.models.Article;
import de.fhdw.models.Picture;
import de.fhdw.util.PictureHandler;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.Cache;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@ApplicationScoped
@Path("article")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Article", description = "Operations on Article object")
public class ArticleImpl implements ArticleInterface {
    private static final Logger LOG = Logger.getLogger(ArticleImpl.class);

    @Override
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"admin", "employee"})
    @Transactional
    @Operation(summary = "registers a new Article Object")
    public Response RegisterNewArticleWithPicture(@MultipartForm ArticleUploadForm data) {
        if (data.article == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        PictureHandler pictureHandler = new PictureHandler();
       Article article = new Article();
        try {
            Picture picture = new Picture(data.getFile(), pictureHandler.scaleImage(data.getFileAsStream()));
            picture.persist();
            article.picture = picture;
            article.artists =data.article.artists;
            article.genre = data.article.genre;
            article.title = data.article.title;
            article.description = data.article.description;
            article.ean = data.article.ean;
            article.price = data.article.price;
            article.persist();
            LOG.debug("added: " + article.toString());
        } catch (Exception e) {
            LOG.info(e.toString());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return Response.accepted(article.id).build();
    }

    @Override
    @GET
    @Cache(maxAge = 5)
    @Operation(summary = "returns all Articles Objects as json with no picture")
    public List<Article> getAllArticlesAsJson() {
        return Article.listAll();
    }

    @GET
    @Override
    @Path("range")
    @Operation(summary = "returns a Range of ArticleForm Objects, including Pictures", description = "example: http://localhost:8080/article/range;start=0;end=20")
    public List<ArticleDownloadForm> getArticleRange(@MatrixParam("start") int start, @MatrixParam("end") int end) {
        PanacheQuery<Article> panacheQuery = Article.findAll();
        panacheQuery.range(start, end);
        List<Article> articles = panacheQuery.list();
        List<ArticleDownloadForm> articleDownloadForms = new ArrayList<>();
        articles.forEach(i -> {
            ArticleDownloadForm articleDownloadForm = new ArticleDownloadForm();
            articleDownloadForm.article = i;
            articleDownloadForm.file = Base64.getEncoder().encodeToString(i.picture.thumbnail);
            articleDownloadForms.add(articleDownloadForm);
        });
        return articleDownloadForms;
    }

    @Override
    @Path("count")
    @Operation(summary = "the total number of available Articles")
    @GET
    public Long countAllArticles() {
        return Article.count();
    }

    @Override
    @GET
    @Path("{id}")
    @Operation(summary = "returns a single Article Object as Multipart Form including the Picture as Byte Array")
    public ArticleDownloadForm getSingleArticle(@PathParam long id) {
        Article article = Article.findById(id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        ArticleDownloadForm articleDownloadForm = new ArticleDownloadForm();
        articleDownloadForm.article = article;
        articleDownloadForm.file = Base64.getEncoder().encodeToString(article.picture.rawData);
        return articleDownloadForm;
    }

    @Override
    @Path("{id}")
    @DELETE
    @Operation(summary = "removes an Article identified by the supplied ID")
    @RolesAllowed({"admin", "employee"})
    public Response deleteArticle(@PathParam long id) {
        Article article = Article.findById(id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        try {
            Picture picture = article.picture;
            picture.delete();
            article.delete();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
        return Response.ok("true").build();
    }

    @Override
    @PUT
    @RolesAllowed({"admin", "employee"})
    @Operation(summary = "changes a Article Object", description = "needs a Multipart Form Picture can be empty")
    @Transactional
    public Article changeArticle(@MultipartForm ArticleUploadForm data) {
        Article article = Article.findById(data.article.id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        article.artists = data.article.artists;
        article.genre = data.article.genre;
        article.title = data.article.title;
        article.price = data.article.price;
        article.ean = data.article.ean;
        article.description = data.article.description;
        try {
            PictureHandler pictureHandler = new PictureHandler();
            article.picture.rawData = data.getFile();
            article.picture.thumbnail = pictureHandler.scaleImage(data.getFileAsStream());
        } catch (Exception e) {

            LOG.error(e.toString());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return Article.findById(data.article.id);
    }


}
