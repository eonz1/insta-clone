package com.cgram.prom.domain.image.service;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageServiceImplTest {
    private String path;
    private File file;
    private String extension;
    private BufferedImage bufferedImage;

    @BeforeEach
    void Setup() throws IOException {
        path = "src/test/resources";
        file = new File(path + File.separator + "mongja.png");
        extension = file.getAbsoluteFile().getName().split("\\.")[1];
        bufferedImage = ImageIO.read(file);
    }

    @Test
    void jpg_파일로_변환하여_저장하기() throws IOException {
        BufferedImage afterImg = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR);
        afterImg.createGraphics().drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),null);

        ImageIO.write(afterImg, "jpg",
            new File(path + File.separator + "convert_mongja.jpg"));
    }
}