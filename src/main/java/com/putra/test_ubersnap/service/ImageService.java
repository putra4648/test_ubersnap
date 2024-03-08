package com.putra.test_ubersnap.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    byte[] convertImage(MultipartFile image) throws IOException;

    byte[] resizeImage(MultipartFile image, int width, int height) throws IOException;

    byte[] compressImage(MultipartFile image) throws IOException;
}
