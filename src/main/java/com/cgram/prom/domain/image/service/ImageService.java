package com.cgram.prom.domain.image.service;

import com.cgram.prom.domain.image.domain.Image;
import java.io.File;
import java.io.IOException;

public interface ImageService {

    Image saveImage(File file) throws IOException;

    File resizeImage(File file, String prefix, int width, int height) throws IOException;
}
