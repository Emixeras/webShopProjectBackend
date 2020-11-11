package de.fhdw.util;

import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class PictureHandler {
    private static final org.jboss.logging.Logger LOG = Logger.getLogger(PictureHandler.class);


    public String checkImageFormat(InputStream stream) throws IOException {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(stream);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            ImageReader reader = readers.next();
            LOG.info(reader.getFormatName());
            return reader.getFormatName();
        }catch (Exception e){
            LOG.info(e.toString());
        }
        return null;
    }

    public byte[] scaleImage(InputStream source, String type) throws IOException {
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
