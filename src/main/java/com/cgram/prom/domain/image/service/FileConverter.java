package com.cgram.prom.domain.image.service;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class FileConverter {

    private final String path = System.getProperty("user.dir") + "/src/main/resources/static/temp";


    public File transferMultipartFileToFile(MultipartFile mFile) {
        try {
            File file = new File(path + File.separator + mFile.getOriginalFilename());
            mFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new RuntimeException("이미지 변환에 실패하였습니다.");
        }
    }
}
