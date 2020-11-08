package de.fhdw.util;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.jboss.logging.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureHandler {
    private static final org.jboss.logging.Logger LOG = Logger.getLogger(PictureHandler.class);


    public String checkImageFormat(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        AutoDetectParser parser = new AutoDetectParser();
        Detector detector = parser.getDetector();
        Metadata md = new Metadata();
        md.add(Metadata.RESOURCE_NAME_KEY, "Picture");
        org.apache.tika.mime.MediaType mediaType = detector.detect(bis, md);
        LOG.info("Picture Type: " + mediaType.toString());
        return mediaType.toString();
    }
}
