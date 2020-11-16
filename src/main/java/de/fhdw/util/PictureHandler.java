package de.fhdw.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

public class PictureHandler {

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


}
