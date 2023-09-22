package com.example.JewelerProgressReport.images;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageUtil {
    private final ResourceLoader resourceLoader;
    private final double scaleFactor = 0.3;

    public byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    private BufferedImage toBufferedImage(byte[] bytes)
            throws IOException {

        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }

    public byte[] saveImage(byte[] imageBytes, String name) throws IOException {
        BufferedImage resizedImage = resizeImage(imageBytes);

        String SAVE_DIRECTORY = resourceLoader.getResource("classpath:/images").getURL().getPath();

        File imageFile = new File(SAVE_DIRECTORY + File.separator + name + ".jpg");
        ImageIO.write(resizedImage, "jpg", imageFile);

        return toByteArray(resizedImage, "jpg");
    }

    public BufferedImage resizeImage(byte[] imageByte) throws IOException {

        BufferedImage originalImage = this.toBufferedImage(imageByte);

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resizedImage;
    }
}
