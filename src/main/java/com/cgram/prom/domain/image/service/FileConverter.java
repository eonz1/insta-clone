package com.cgram.prom.domain.image.service;

import com.cgram.prom.domain.image.model.ImageProperties;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileConverter {

    private final ImageProperties imageProperties;

    public File transferMultipartFileToFile(MultipartFile mFile) {
        System.out.println("path= "+imageProperties.getDirectory());
        try {
            File file = new File(imageProperties.getDirectory() + File.separator + "temp" + File.separator + mFile.getOriginalFilename());
            mFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("이미지 변환에 실패하였습니다.", e);
        }
    }
}
