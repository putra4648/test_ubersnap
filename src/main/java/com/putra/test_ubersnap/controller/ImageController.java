package com.putra.test_ubersnap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.putra.test_ubersnap.constant.ImageConstant;
import com.putra.test_ubersnap.response.ValidatorResponse;
import com.putra.test_ubersnap.service.ImageService;



@Controller
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("convert")
    public ResponseEntity<?> convertImage(@RequestParam("image") Optional<MultipartFile> image) {
        if (image.isPresent()) {
            String mimeType = image.get().getContentType();
            if (MimeTypeUtils.IMAGE_PNG_VALUE.equalsIgnoreCase(mimeType)) {
                String filename =
                        FilenameUtils.getBaseName(image.get().getOriginalFilename()) + ".jpeg";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentDisposition(
                        ContentDisposition.attachment().filename(filename).build());
                return ResponseEntity.ok().headers(headers)
                        .body(imageService.convertImage(image.get()));
            } else {
                return ResponseEntity.badRequest().body("Only PNG is supported");
            }
        } else {
            return ResponseEntity.badRequest().body(ImageConstant.ERROR_IMAGE_PARAM);
        }
    }

    @PostMapping("resize")
    public ResponseEntity<?> resizeImage(@RequestParam("width") Optional<Integer> width,
            @RequestParam("height") Optional<Integer> height,
            @RequestParam("image") Optional<MultipartFile> image) {
        boolean isValid = width.isPresent() && height.isPresent() && image.isPresent();

        if (isValid) {
            String mimeType = image.get().getContentType();
            if (MimeTypeUtils.IMAGE_PNG_VALUE.equalsIgnoreCase(mimeType)
                    || MimeTypeUtils.IMAGE_JPEG_VALUE.equalsIgnoreCase(mimeType)) {
                String ext = FilenameUtils.getExtension(image.get().getOriginalFilename());
                MediaType contentType =
                        ext.equalsIgnoreCase("jpeg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(contentType);
                headers.setContentDisposition(ContentDisposition.attachment()
                        .filename(image.get().getOriginalFilename()).build());
                return ResponseEntity.ok().headers(headers)
                        .body(imageService.resizeImage(image.get(), width.get(), height.get()));
            } else {
                return ResponseEntity.ok().body(ImageConstant.ERROR_IMAGE_NOT_SUPPORTED);
            }
        } else {
            List<ValidatorResponse> errors = new ArrayList<>();
            ValidatorResponse obj = null;

            if (!width.isPresent()) {
                obj = new ValidatorResponse("width", "'width' paramater is required");
                errors.add(obj);
            }

            if (!height.isPresent()) {
                obj = new ValidatorResponse("height", "'height' paramater is required");
                errors.add(obj);
            }

            if (!image.isPresent()) {
                obj = new ValidatorResponse("image", "'image' paramater is required");
                errors.add(obj);
            }
            return ResponseEntity.badRequest().body(errors);
        }
    }

    @PostMapping("compress")
    public ResponseEntity<?> compressImage(@RequestParam("image") Optional<MultipartFile> image) {
        if (image.isPresent()) {
            String mimeType = image.get().getContentType();
            if (MimeTypeUtils.IMAGE_PNG_VALUE.equalsIgnoreCase(mimeType)
                    || MimeTypeUtils.IMAGE_JPEG_VALUE.equalsIgnoreCase(mimeType)) {
                String ext = FilenameUtils.getExtension(image.get().getOriginalFilename());
                MediaType contentType =
                        ext.equalsIgnoreCase("jpeg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(contentType);
                headers.setContentDisposition(ContentDisposition.attachment()
                        .filename(image.get().getOriginalFilename()).build());
                return ResponseEntity.ok().headers(headers)
                        .body(imageService.compressImage(image.get()));
            } else {
                return ResponseEntity.badRequest().body(ImageConstant.ERROR_IMAGE_NOT_SUPPORTED);
            }
        } else {
            return ResponseEntity.badRequest().body(ImageConstant.ERROR_IMAGE_PARAM);
        }
    }

}
