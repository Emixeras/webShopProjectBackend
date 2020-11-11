package de.fhdw.util;

import io.quarkus.runtime.annotations.RegisterForReflection;
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

@RegisterForReflection
public class PictureHandler {
    private static final org.jboss.logging.Logger LOG = Logger.getLogger(PictureHandler.class);


    public String checkImageFormat(InputStream stream) throws IOException {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(stream);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            ImageReader reader = readers.next();
            LOG.debug(reader.getFormatName());
            return reader.getFormatName();
        } catch (Exception e) {
            LOG.debug(e.toString());
        }
        return null;
    }

    public byte[] scaleImage(InputStream source, String type) throws IOException {
        try {
            BufferedImage bImageFromConvert = ImageIO.read(source);
            bImageFromConvert = scale(bImageFromConvert, 0.25);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bImageFromConvert, type, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }

    }

    private BufferedImage scale(BufferedImage source, double ratio) {
        try {
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
        } catch (Exception e) {
            LOG.error(e.toString());
            return null;
        }

    }

    private BufferedImage getCompatibleImage(int w, int h) {

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            return gc.createCompatibleImage(w, h);
        } catch (Exception e) {
            LOG.error(e.toString());
            return null;
        }


    }

}
