package de.fhdw.util;

import net.coobird.thumbnailator.Thumbnails;
import org.jboss.logging.Logger;
import java.io.*;

public class PictureHandler {
    private static final org.jboss.logging.Logger LOG = Logger.getLogger(PictureHandler.class);

    public byte[] scaleImage(InputStream source ) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(source)
                .size(640, 480)
                .outputFormat("png")
                .toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }


}
