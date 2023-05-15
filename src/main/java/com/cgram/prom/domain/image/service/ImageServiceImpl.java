package com.cgram.prom.domain.image.service;

import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.repository.ImageRepository;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ResizeImageGenerator ResizeProfileImageGenerator = new ResizeImageGenerator(110, 110);
    private final ResizeImageGenerator ResizeFeedImageGenerator = new ResizeImageGenerator(1080, 1080);
    private String path = System.getProperty("user.dir")+"/src/main/resources/static";

    @Transactional
    public Image saveImage(File file) throws IOException {
        Image image = imageRepository.save(Image.builder().path(path).build());

        String extension = file.getAbsoluteFile().getName().split("\\.")[1];

        BufferedImage bufferedImage = convertingToJpg(ImageIO.read(file), extension);
        ImageIO.write(bufferedImage, "jpg",
            new File(image.getPath() + File.separator + image.getId() + ".jpg"));

        return image;
    }

    private BufferedImage convertingToJpg(BufferedImage bufferedImage, String extension) {
        if(extension.equals("jpg"))
            return bufferedImage;

        BufferedImage afterImg = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR);
        afterImg.createGraphics().drawImage(bufferedImage, 0, 0,null);

        return afterImg;
    }
}
