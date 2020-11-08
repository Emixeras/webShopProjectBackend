package de.fhdw.util;

import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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

    public byte[] scaleAbleImage(InputStream source, String type) throws IOException {
        BufferedImage bImageFromConvert = ImageIO.read(source);
        bImageFromConvert = scale(bImageFromConvert, 0.25);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bImageFromConvert, type, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private BufferedImage scale(BufferedImage source, double ratio) {
        int w = (int) (source.getWidth() * ratio);
        int h = (int) (source.getHeight() * ratio);
        BufferedImage bi = getCompatibleImage(w, h);
        Graphics2D g2d = bi.createGraphics();
        double xScale = (double) w / source.getWidth();
        double yScale = (double) h / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }

    private BufferedImage getCompatibleImage(int w, int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleImage(w, h);

    }

}
