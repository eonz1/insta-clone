package com.cgram.prom.domain.image.service;

import com.cgram.prom.domain.image.domain.Image;
import com.cgram.prom.domain.image.model.ImageProperties;
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
    private final ImageProperties imageProperties;

    @Transactional
    public Image saveImage(File file) throws IOException {
        Image image = imageRepository.save(Image.builder().path(imageProperties.getDirectory()).isPresent(true).build());

        String extension = file.getAbsoluteFile().getName().split("\\.")[1];

        BufferedImage bufferedImage = newBufferedImageForSaveJpg(ImageIO.read(file), extension);
        ImageIO.write(bufferedImage, "jpg",
            new File(image.getPath() + File.separator + image.getId() + ".jpg"));

        return image;
    }

    private BufferedImage newBufferedImageForSaveJpg(BufferedImage bufferedImage,
        String extension) {

        if (extension.equals("jpg")) {
            return bufferedImage;
        }

        BufferedImage afterImg = new BufferedImage(bufferedImage.getWidth(),
            bufferedImage.getHeight()
            , BufferedImage.TYPE_3BYTE_BGR);
        afterImg.createGraphics().drawImage(bufferedImage, 0, 0, null);

        return afterImg;
    }

    public File resizeImage(File file, String prefix, int width, int height) throws IOException {
        ResizeImageGenerator resizeImageGenerator = new ResizeImageGenerator(width, height);
        BufferedImage resizeImage = resizeImageGenerator.generate(file);

        String resizeImageName = prefix + file.getName();
        String path = file.getPath().split(file.getName())[0];

        File coverFile = new File(path + File.separator + resizeImageName);
        ImageIO.write(resizeImage, file.getPath().split("\\.")[1], coverFile);

        return coverFile;
    }
}
