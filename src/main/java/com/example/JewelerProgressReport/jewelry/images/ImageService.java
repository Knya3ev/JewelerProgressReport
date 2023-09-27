package com.example.JewelerProgressReport.jewelry.images;


import com.example.JewelerProgressReport.parser.ParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ParserService parserService;
    private final ImageUtil imageUtil;
    private final ResourceLoader resourceLoader;

    public byte[] getImage(String article) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/images" + File.separator + "%s.jpg".formatted(article));

        if (resource.exists()) {
            return readImagesAsBytes(resource);
        }

        byte[] image = parserService.getImage(article);

        if (image == null) {
            return getDefaultImage();
        }

        return saveImage(image, article);
    }

    private byte[] saveImage(byte[] imageBytes, String name) throws IOException {
        BufferedImage resizedImage = imageUtil.resizeImage(imageBytes);

        String SAVE_DIRECTORY = resourceLoader.getResource("classpath:/images").getURL().getPath();

        File imageFile = new File(SAVE_DIRECTORY + File.separator + name + ".jpg");
        ImageIO.write(resizedImage, "jpg", imageFile);

        return imageUtil.toByteArray(resizedImage, "jpg");
    }

    private byte[] readImagesAsBytes(Resource resource) throws IOException {

        InputStream inputStream = resource.getInputStream();
        return inputStream.readAllBytes();

    }

    private byte[] getDefaultImage() throws IOException {
        return readImagesAsBytes(resourceLoader.getResource("classpath:/images" + File.separator + "default_image.jpg"));
    }

    public String getImageLink(String article) {
        return parserService.getImageLink(article);
    }
}
