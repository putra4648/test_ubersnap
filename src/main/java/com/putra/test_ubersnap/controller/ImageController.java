package com.putra.test_ubersnap.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.putra.test_ubersnap.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;



@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/get-image/{path}")
    public ResponseEntity<?> getImage(@PathVariable String path) {
        System.out.println(path);
        try {
            String localPathString = new FileSystemResource("").getFile().getAbsolutePath();
            Path localPath = Paths.get(localPathString, path);
            System.out.println("Local Path " + localPathString);
            File file = localPath.toFile();
            byte[] data = Files.readAllBytes(file.toPath());
            return ResponseEntity.ok().body(data);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("File not found");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Cannot proccess file");
        }

    }


    @PostMapping("convert")
    public ResponseEntity<?> convertImage(@RequestParam("image") Optional<MultipartFile> image,
            HttpServletResponse response) {

        // Check if image is exist and not null
        if (image.isPresent()) {
            String mimeType = image.get().getContentType();

            if (MimeTypeUtils.IMAGE_PNG_VALUE.equalsIgnoreCase(mimeType)) {
                try {
                    String filename =
                            FilenameUtils.getBaseName(image.get().getOriginalFilename()) + ".jpg";
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=" + filename)
                            .body(imageService.convertImage(image.get()));
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            } else {
                return ResponseEntity.ok().body("Only PNG is supported");
            }

        } else {
            return ResponseEntity.badRequest().body("Image paramater is not exist");
        }
    }



    @PostMapping("resize")
    public ResponseEntity<?> resizeImage(@RequestParam("width") Integer width,
            @RequestParam("height") Integer height,
            @RequestParam("image") Optional<MultipartFile> image, HttpServletResponse response) {
        // Check if image is exist and not null
        if (image.isPresent()) {
            String mimeType = image.get().getContentType();

            if (MimeTypeUtils.IMAGE_PNG_VALUE.equalsIgnoreCase(mimeType)
                    || MimeTypeUtils.IMAGE_JPEG_VALUE.equalsIgnoreCase(mimeType)) {
                try {
                    String ext = FilenameUtils.getExtension(image.get().getOriginalFilename());
                    MediaType contentType =
                            ext == "jpg" ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;

                    return ResponseEntity.ok().contentType(contentType)
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=" + image.get().getOriginalFilename())
                            .body(imageService.resizeImage(image.get(), width, height));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            } else {
                return ResponseEntity.ok().body("Image is not supported yet");
            }

        } else {
            return ResponseEntity.badRequest().body("Image paramater is not exist");
        }
    }

    @PostMapping("compress")
    public ResponseEntity compressImage(@RequestParam("image") Optional<MultipartFile> image) {
        // Check if image is exist and not null
        if (image.isPresent()) {
            String mimeType = image.get().getContentType();

            if (MimeTypeUtils.IMAGE_PNG_VALUE.equalsIgnoreCase(mimeType)
                    || MimeTypeUtils.IMAGE_JPEG_VALUE.equalsIgnoreCase(mimeType)) {
                try {
                    String ext = FilenameUtils.getExtension(image.get().getOriginalFilename());
                    MediaType contentType =
                            ext == "jpg" ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
                    return ResponseEntity.ok().contentType(contentType)
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=" + image.get().getOriginalFilename())
                            .body(imageService.compressImage(image.get()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            } else {
                return ResponseEntity.ok().body("Image is not supported yet");
            }

        } else {
            return ResponseEntity.badRequest().body("Image paramater is not exist");
        }
    }

}
