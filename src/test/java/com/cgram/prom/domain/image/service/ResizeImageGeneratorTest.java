package com.cgram.prom.domain.image.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResizeImageGeneratorTest {

    private ResizeImageGenerator generator;
    private String path;
    private File originalFile;

    @BeforeEach
    void Setup() {
        generator = new ResizeImageGenerator();
        path = "src/test/resources";
        originalFile = new File(path + File.separator + "mongja.png");
    }

    @Test
    void 이미지_너비와_높이를_조절한다() throws IOException {
        generator = new ResizeImageGenerator(500, 500);
        BufferedImage resizeImage = generator.generate(originalFile);

        ImageIO.write(resizeImage, "png", new File(path + File.separator + "resize_mongja.png"));
    }
}