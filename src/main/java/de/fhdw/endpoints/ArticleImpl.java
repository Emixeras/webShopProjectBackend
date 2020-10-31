package de.fhdw.endpoints;

import de.fhdw.forms.ArticleForm;
import de.fhdw.models.ArticleMetadata;
import de.fhdw.models.ArticlePicture;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@ApplicationScoped
@Path("article")
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class ArticleImpl implements ArticleInterface{
    private static final Logger LOG = Logger.getLogger(ArticleImpl.class);

    @Override
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public String createArticle(@MultipartForm ArticleForm data) throws IOException {
        LOG.info("ich wurde aufgerufen");
        data.article.persist();
        ArticlePicture articlePicture = new ArticlePicture();
        articlePicture.picture = IOUtils.toByteArray(data.file);
        articlePicture.articleMetadata = data.article;
        articlePicture.persist();
        return "Upload Erfolgreich";
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
