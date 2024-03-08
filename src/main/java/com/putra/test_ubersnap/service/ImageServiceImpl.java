package com.putra.test_ubersnap.service;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("imageService")
public class ImageServiceImpl implements ImageService {
    private final String LOCAL_PATH = new FileSystemResource("").getFile().getAbsolutePath();


    @Override
    public byte[] convertImage(MultipartFile image) throws IOException {
        BufferedImage imageData = ImageIO.read(image.getInputStream());

        BufferedImage convertedImage = new BufferedImage(imageData.getWidth(),
                imageData.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(imageData, 0, 0, Color.WHITE, null);

        Path localPath = Paths.get(LOCAL_PATH, "images/convert", image.getOriginalFilename());

        ImageIO.write(convertedImage, "jpg", localPath.toFile());


        return Files.readAllBytes(localPath);
    }

    @Override
    public byte[] resizeImage(MultipartFile image, int width, int height) throws IOException {

        BufferedImage imageData = ImageIO.read(image.getInputStream());
        Image img = imageData.getScaledInstance(width, height, Image.SCALE_DEFAULT);

        BufferedImage convertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        convertedImage.getGraphics().drawImage(img, 0, 0, null);

        String newFilename =
                FilenameUtils.getBaseName(image.getOriginalFilename()) + "_resize_" + width + "x"
                        + height + "." + FilenameUtils.getExtension(image.getOriginalFilename());

        Path localPath = Paths.get(LOCAL_PATH, "images/resize", newFilename);


        ImageIO.write(convertedImage, FilenameUtils.getExtension(image.getOriginalFilename()),
                localPath.toFile());

        return Files.readAllBytes(localPath);

    }

    @Override
    public byte[] compressImage(MultipartFile image) throws IOException {
        BufferedImage imageData = ImageIO.read(image.getInputStream());

        BufferedImage convertedImage = new BufferedImage(imageData.getWidth(),
                imageData.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(imageData, 0, 0, Color.WHITE, null);

        String newFilename = FilenameUtils.getBaseName(image.getOriginalFilename()) + "_compress"
                + "." + FilenameUtils.getExtension(image.getOriginalFilename());
        Path localPath = Paths.get(LOCAL_PATH, "images/compress", newFilename);

        ImageIO.write(convertedImage, "jpg", localPath.toFile());


        return Files.readAllBytes(localPath);
    }

}
