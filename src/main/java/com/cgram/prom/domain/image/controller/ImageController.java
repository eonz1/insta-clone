package com.cgram.prom.domain.image.controller;

import com.cgram.prom.domain.image.service.ImageService;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) throws IOException {

        File file = imageService.getImage(id);
        byte[] imageFileByteArray = FileCopyUtils.copyToByteArray(file);

        return ResponseEntity.ok().body(imageFileByteArray);
    }

}
