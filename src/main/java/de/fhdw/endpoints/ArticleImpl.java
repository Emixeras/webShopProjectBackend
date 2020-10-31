package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.ArticleMetadata;
import de.fhdw.models.ArticlePicture;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("article")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.MULTIPART_FORM_DATA)
public class ArticleImpl implements ArticleInterface{

    @Override
    @Path("test")

    @POST
    public MultipartOutput createArticle(@MultipartForm ArticleForm data) {
        ArticleMetadata articleMetadata = data.Article;
        articleMetadata.persist();
        ArticlePicture articlePicture = new ArticlePicture();
        articlePicture.picture = data.picture;
        articlePicture.articleMetadata = articleMetadata;
        articlePicture.persist();

        MultipartOutput multipartOutput = new MultipartOutput();
        multipartOutput.addPart(articleMetadata, MediaType.valueOf(MediaType.APPLICATION_JSON));
        multipartOutput.addPart(articlePicture.picture, MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM));
        return multipartOutput;
    }


    @Override
    public Response ChangeArticle() {
        return null;
    }

    @Override
    @GET
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public MultipartOutput getArticle() {
        MultipartOutput output = new MultipartOutput();
        output.addPart("bill", MediaType.valueOf(MediaType.TEXT_PLAIN));
        output.addPart("monica",MediaType.valueOf(MediaType.TEXT_PLAIN));
        return output;
    }
}
