package de.fhdw.util;

import net.coobird.thumbnailator.Thumbnails;
import java.io.*;

public class PictureHandler {

    public byte[] scaleImage(InputStream source ) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(source)
                .size(300, 300)
                .outputFormat("png")
                .toOutputStream(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }


}
