package de.fhdw.util;

import de.fhdw.models.ShopOrderArticle;
import net.coobird.thumbnailator.Thumbnails;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;

@ApplicationScoped
public class PictureHandler {

    private static final Logger LOG = Logger.getLogger(PictureHandler.class);

    public byte[] scaleImage(InputStream source) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(source)
                .size(250, 250)
                .outputFormat("png")
                .toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] scaleImage(InputStream source, int quality) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Thumbnails.of(source)
                .size(quality, quality)
                .outputFormat("png")
                .toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] scaleImage(byte[] source, int quality) {
       try {
           InputStream inputStream = new ByteArrayInputStream(source);
           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

           Thumbnails.of(inputStream)
                   .size(quality, quality)
                   .outputFormat("png")
                   .toOutputStream(byteArrayOutputStream);
           return byteArrayOutputStream.toByteArray();

       }catch (Exception e){
           LOG.error(e.toString());
       }
       return null;
    }


}
