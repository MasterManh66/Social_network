package com.social_luvina.social_dev8.modules.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.social_luvina.social_dev8.modules.models.dto.request.UploadImageRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.services.impl.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Image Upload", description = "APIs for uploading images")
public class UploadImageController {
  
  ImageService imageService;

    @Operation(summary = "Upload Image", description = "Upload one or multiple images")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UploadImageRequest>> uploadImage(@RequestParam("files") List<MultipartFile> files) throws IOException {
        return imageService.uploadFiles(files);
    }

    @Operation(summary = "Download Image", description = "Download an image by filename")
    @GetMapping(value = "/download")
    public ResponseEntity<?> getImage(@RequestParam("filename") String filename) {
        return imageService.downloadImage(filename);
    }

    @Operation(summary = "Delete Image", description = "Delete an image by filename")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteImageFile(@RequestParam("filename") String filename) {
        return imageService.deleteImageFile(filename);
    }
}
