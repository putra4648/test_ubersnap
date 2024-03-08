package com.putra.test_ubersnap.service;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
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
        public byte[] convertImage(MultipartFile image) {
                String newFilename =
                                FilenameUtils.getBaseName(image.getOriginalFilename()) + ".jpeg";
                Path localPath = Paths.get(LOCAL_PATH, "images/convert", newFilename);
                File localFile = localPath.toFile();
                BufferedImage imageData = null;
                try {
                        imageData = ImageIO.read(image.getInputStream());
                        BufferedImage convertedImage = new BufferedImage(imageData.getWidth(),
                                        imageData.getHeight(), BufferedImage.TYPE_INT_RGB);
                        convertedImage.createGraphics().drawImage(imageData, 0, 0, Color.WHITE,
                                        null);
                        ImageIO.write(convertedImage, "jpeg", localFile);
                        convertedImage.flush();
                        return Files.readAllBytes(localPath);
                } catch (Exception e) {
                        if (!localFile.exists()) {
                                localFile.mkdirs();
                        }
                        return convertImage(image);
                }
        }

        @Override
        public byte[] resizeImage(MultipartFile image, int width, int height) {
                String newFilename = FilenameUtils.getBaseName(image.getOriginalFilename())
                                + "_resize_" + width + "x" + height + "."
                                + FilenameUtils.getExtension(image.getOriginalFilename());
                Path localPath = Paths.get(LOCAL_PATH, "images/resize", newFilename);
                File localFile = localPath.toFile();
                BufferedImage imageData = null;
                try {
                        imageData = ImageIO.read(image.getInputStream());
                        Image img = imageData.getScaledInstance(width, height, Image.SCALE_DEFAULT);

                        BufferedImage convertedImage = new BufferedImage(width, height,
                                        BufferedImage.TYPE_INT_RGB);
                        convertedImage.getGraphics().drawImage(img, 0, 0, null);
                        ImageIO.write(convertedImage,
                                        FilenameUtils.getExtension(image.getOriginalFilename()),
                                        localFile);
                        convertedImage.flush();
                        return Files.readAllBytes(localPath);
                } catch (Exception e) {
                        if (!localFile.exists()) {
                                localFile.mkdirs();
                        }
                        return resizeImage(image, width, height);
                }
        }

        @Override
        public byte[] compressImage(MultipartFile image) {
                String newFilename = FilenameUtils.getBaseName(image.getOriginalFilename())
                                + "_compress" + "."
                                + FilenameUtils.getExtension(image.getOriginalFilename());
                Path localPath = Paths.get(LOCAL_PATH, "images/compress", newFilename);
                File localFile = localPath.toFile();
                try {
                        BufferedImage imageData = ImageIO.read(image.getInputStream());
                        BufferedImage convertedImage = new BufferedImage(imageData.getWidth(),
                                        imageData.getHeight(), BufferedImage.TYPE_INT_RGB);
                        convertedImage.createGraphics().drawImage(imageData, 0, 0, Color.WHITE,
                                        null);
                        ImageIO.write(convertedImage, "jpeg", localFile);
                        convertedImage.flush();
                        return Files.readAllBytes(localPath);
                } catch (Exception e) {
                        if (!localFile.exists()) {
                                localFile.mkdirs();
                        }
                        return compressImage(image);
                }
        }

}
