package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.Article;
import de.fhdw.models.Artist;
import de.fhdw.models.Genre;
import de.fhdw.models.Picture;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
@Path("article")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class ArticleImpl implements ArticleInterface {
    private static final Logger LOG = Logger.getLogger(ArticleImpl.class);

    @Override
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    //todo: @RolesAllowed({"admin", "employee"})
    @Transactional
    public Response post(@MultipartForm ArticleForm data) throws IOException {
        LOG.info("ich wurde aufgerufen");
        Article article = data.article;
        setNewValues(data, article);
        article.persist();
        Picture picture = new Picture();
        picture.value = IOUtils.toByteArray(data.file);
        article.picture = picture;
        picture.persist();
        return Response.accepted(data.article.id).build();
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
        articleForm.file = new ByteArrayInputStream(article.picture.value);
        return articleForm;
    }

    @Override
    @Path("{id}")
    @DELETE
    //todo: @RolesAllowed({"admin", "employee"})
    public Response delete(@PathParam long id) {
        Article article = Article.findById(id);
        if (article == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        try {
            article.delete();
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.EXPECTATION_FAILED);
        }
        return Response.ok("true").build();
    }


    @Override
    @PUT
    //todo: @RolesAllowed({"admin", "employee"})
    public Response put(@MultipartForm ArticleForm data) throws IOException {
        Article article = Article.findById(data.article.id);
        if(article == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        setNewValues(data, article);
        article.artists = data.article.artists;
        article.genre = data.article.genre;
        article.title = data.article.title;
        article.price = data.article.price;
        article.ean = data.article.ean;
        article.description = data.article.description;
        Picture picture = article.picture;
        picture.value = IOUtils.toByteArray(data.file);
        article.picture = picture;
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
        LOG.info(data.article.genre.name);
        if (article.artists != null) {
            if (Artist.findByName(article.artists.name) != null) {
                article.artists = Artist.findByName(article.artists.name);
            } else {
                Artist artist = new Artist(article.artists.name);
                artist.persist();
                article.artists = artist;
            }
        }
        LOG.info(data.article.artists.name);
    }


}
