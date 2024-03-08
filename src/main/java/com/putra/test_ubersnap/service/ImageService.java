package com.putra.test_ubersnap.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    byte[] convertImage(MultipartFile image);

    byte[] resizeImage(MultipartFile image, int width, int height);

    byte[] compressImage(MultipartFile image);
}
