package com.cgram.prom.domain.image.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class ResizeImageGenerator {

    private int width;
    private int height;

    public ResizeImageGenerator() {
        this(200, 200);
    }

    public ResizeImageGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public BufferedImage generate(File originalFile) throws IOException {
        BufferedImage bufferedOriginalImage = ImageIO.read(originalFile);
        BufferedImage bufferedThumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D graphic = bufferedThumbnailImage.createGraphics();
        graphic.drawImage(bufferedOriginalImage, 0, 0, width, height, null);
        graphic.dispose();

        return bufferedThumbnailImage;
    }
}
