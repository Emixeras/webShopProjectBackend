package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.Article;
import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
import de.fhdw.models.Picture;
import de.fhdw.util.PictureHandler;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
@Path("article")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name="Article",description = "Operations on Article object")
public class ArticleImpl implements ArticleInterface {
    private static final Logger LOG = Logger.getLogger(ArticleImpl.class);


    @Override
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    // @RolesAllowed({"admin", "employee"})
    @Transactional
    public Response post(@MultipartForm ArticleForm data) throws IOException {
        if (data.article == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        PictureHandler pictureHandler = new PictureHandler();
        String media = pictureHandler.checkImageFormat(data.getFileAsStream());

        if (media.equals("image/png") || media.equals("image/jpeg")) {
            Article article = data.article;
            setNewValues(data, article);
            LOG.info("added: " + article.toString());
            Picture picture = new Picture(data.getFile());
            picture.persist();
            article.image = picture;
            article.persist();
            return Response.accepted(data.article.id).build();
        }
        throw new WebApplicationException(Response.Status.BAD_REQUEST);


    }

    @Override
    @GET

    public List<Article> get() {
        return Article.listAll();
    }

    @Override
    @GET
    @Path("{id}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public ArticleForm get(@PathParam long id) {
        Article article = Article.findById(id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        ArticleForm articleForm = new ArticleForm();
        articleForm.article = article;
        articleForm.setFile(article.image.data);
        return articleForm;
    }

    @Override
    @Path("{id}")
    @DELETE
    @RolesAllowed({"admin", "employee"})
    public Response delete(@PathParam long id) {
        Article article = Article.findById(id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        try {
            Picture picture = article.image;
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
    public Response put(@MultipartForm ArticleForm data) throws IOException {
        Article article = Article.findById(data.article.id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        setNewValues(data, article);
        article.artists = data.article.artists;
        article.genre = data.article.genre;
        article.title = data.article.title;
        article.price = data.article.price;
        article.ean = data.article.ean;
        article.description = data.article.description;

        PictureHandler pictureHandler = new PictureHandler();
        String media = pictureHandler.checkImageFormat(data.getFileAsStream());
        if (media.equals("image/png") || media.equals("image/jpeg")) {
            article.image.data = data.getFile();
        }

        return Response.accepted(data.article.id).build();
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
