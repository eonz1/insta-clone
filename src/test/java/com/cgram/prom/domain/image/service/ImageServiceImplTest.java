package com.cgram.prom.domain.image.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    private String path;
    private File file;
    private String extension;
    private BufferedImage bufferedImage;

    @InjectMocks
    ImageServiceImpl imageService;

    @BeforeEach
    void Setup() throws IOException {
        path = "src/test/resources";
        file = new File(path + File.separator + "mongja.png");
        extension = file.getAbsoluteFile().getName().split("\\.")[1];
        bufferedImage = ImageIO.read(file);
    }

    @Test
    void jpg_파일로_변환하여_저장하기() throws IOException {
        BufferedImage afterImg = new BufferedImage(bufferedImage.getWidth(),
            bufferedImage.getHeight(),
            BufferedImage.TYPE_3BYTE_BGR);
        afterImg.createGraphics()
            .drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                null);

        ImageIO.write(afterImg, "jpg",
            new File(path + File.separator + "convert_mongja.jpg"));
    }

    @Test
    @DisplayName("원하는 크기로 이미지 리사이즈해서 prefix 붙여서 저장하기")
    public void resizeCoverImage() throws Exception {
        // given
        int width = 250;
        int height = 250;

        // when
        File resizedImage = imageService.resizeImage(file, "cover_", width, height);

        // then
        Image image = new ImageIcon(resizedImage.getAbsolutePath()).getImage();
        assertThat(image.getWidth(null)).isEqualTo(width);
        assertThat(image.getHeight(null)).isEqualTo(height);

        assertThat(resizedImage.getName()).isEqualTo("cover_" + file.getName());
    }
}