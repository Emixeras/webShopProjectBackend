package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.Article;
import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
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
    //@RolesAllowed({"admin", "employee"})
    @Transactional
    @Operation(summary = "registers a new Article Object")
    public Response post(@MultipartForm ArticleForm data) {
        if (data.article == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        PictureHandler pictureHandler = new PictureHandler();
        String media;
        try {
            media = pictureHandler.checkImageFormat(data.getFileAsStream());
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        if (media.equals("png") || media.equals("JPEG")) {
            Article article = data.article;
            setNewValues(data, article);
            LOG.info("added: " + article.toString());
            try {
                String type = media.equals("JPEG") ? "jpg" : "png";
                Picture picture = new Picture(data.getFile(), pictureHandler.scaleImage(data.getFileAsStream(), type));
                picture.persist();
                article.picture = picture;
                article.persist();
                return Response.accepted(data.article.id).build();
            } catch (Exception e) {
                LOG.info(e.toString());
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    @GET
    @Cache(maxAge = 5)
    @Operation(summary = "returns all Articles Objects as json with no picture")
    public List<Article> getAll() {
        return Article.listAll();
    }

    @GET
    @Override
    @Path("range")
    @Operation(summary = "returns a Range of ArticleForm Objects, including Pictures", description = "example: http://localhost:8080/article/range;start=0;end=20")
    public List<ArticleForm> getRange(@MatrixParam("start") int start, @MatrixParam("end") int end) {
        PanacheQuery<Article> panacheQuery = Article.findAll();
        panacheQuery.range(start, end);
        List<Article> articles = panacheQuery.list();
        List<ArticleForm> articleForms = new ArrayList<>();
        articles.forEach(i -> {
            ArticleForm articleForm = new ArticleForm();
            articleForm.article = i;
            articleForm.setFile(i.picture.thumbnail);
            articleForms.add(articleForm);
        });
        return articleForms;
    }

    @Override
    @Path("count")
    @Operation(summary = "the total number of available Articles")
    @GET
    public Long getCount() {
        return Article.count();
    }

    @Override
    @GET
    @Path("{id}")
    @Operation(summary = "returns a single Article Object as Multipart Form including the Picture as Byte Array")
    public ArticleForm getSingle(@PathParam long id) {
        Article article = Article.findById(id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        ArticleForm articleForm = new ArticleForm();
        articleForm.article = article;
        articleForm.setFile(article.picture.rawData);
        return articleForm;
    }

    @Override
    @Path("{id}")
    @DELETE
    @Operation(summary = "removes an Article identified by the supplied ID")
    @RolesAllowed({"admin", "employee"})
    public Response delete(@PathParam long id) {
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
    public Response put(@MultipartForm ArticleForm data) {
        Article article = Article.findById(data.article.id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        setNewValues(data, article); //todo: remove this after article and genre Page is ready
        article.artists = data.article.artists;
        article.genre = data.article.genre;
        article.title = data.article.title;
        article.price = data.article.price;
        article.ean = data.article.ean;
        article.description = data.article.description;
        PictureHandler pictureHandler = new PictureHandler();
        try {
            String media = pictureHandler.checkImageFormat(data.getFileAsStream());
            if (media.equals("png") || media.equals("JPEG")) {
                article.picture.rawData = data.getFile();
            }

            return Response.accepted(data.article.id).build();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

    }

    private void setNewValues(@MultipartForm ArticleForm data, Article article) {
        if (article.genre != null) {
            if (Genre.findByName(article.genre.name) != null) {
                article.genre = Genre.findByName(article.genre.name);
            } else {
                Genre genre = new Genre(article.genre.name);
                genre.persist();
                article.genre = genre;
            }
        }
        LOG.debug(data.article.genre.name);
        if (article.artists != null) {
            if (Artist.findByName(article.artists.name) != null) {
                article.artists = Artist.findByName(article.artists.name);
            } else {
                Artist artist = new Artist(article.artists.name);
                artist.persist();
                article.artists = artist;
            }
        }
        LOG.debug(data.article.artists.name);
    }


}
