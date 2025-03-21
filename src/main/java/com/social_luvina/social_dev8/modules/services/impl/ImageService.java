package com.social_luvina.social_dev8.modules.services.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.social_luvina.social_dev8.modules.models.dto.request.UploadImageRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
  
  private static final String UPLOAD_DIR = "uploads/";

  public ResponseEntity<ApiResponse<UploadImageRequest>> uploadFiles(List<MultipartFile> files) {
    List<String> uploadedImages = new ArrayList<>();
        
    try {
      if (files == null || files.isEmpty()) {
        return ResponseEntity.badRequest().body(
              ApiResponse.<UploadImageRequest>builder()
              .status(HttpStatus.BAD_REQUEST.value())
              .message("Danh sách ảnh trống!")
              .build()
        );
      }

      for (MultipartFile file : files) {
        if (file.isEmpty()) {
            continue;
        }

      long maxSize = 5 * 1024 * 1024;
          if (file.getSize() > maxSize) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(
                  ApiResponse.<UploadImageRequest>builder()
                  .status(HttpStatus.PAYLOAD_TOO_LARGE.value())
                  .message("Ảnh '" + file.getOriginalFilename() + "' vượt quá dung lượng tối đa (5MB)")
                  .build()
            );
          }

      BufferedImage image = ImageIO.read(file.getInputStream());
      String contentType = file.getContentType();
          if (contentType == null || !isImage(contentType) || image == null) {
              return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(
                    ApiResponse.<UploadImageRequest>builder()
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .message("File '" + file.getOriginalFilename() + "' không phải là ảnh hợp lệ")
                    .build()
              );
          }

          // Tạo tên file mới với UUID
          String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
          Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();

          // Kiểm tra và tạo thư mục nếu chưa có
          if (!Files.exists(Paths.get(UPLOAD_DIR))) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
          }

          // Lưu file vào thư mục
          Files.write(filePath, file.getBytes());
            uploadedImages.add(fileName);
          }

          // Trả về danh sách ảnh đã tải lên
          UploadImageRequest response = new UploadImageRequest(uploadedImages);
            return ResponseEntity.ok(
                ApiResponse.<UploadImageRequest>builder()
                .status(HttpStatus.OK.value())
                .message("Tải ảnh thành công!")
                .data(response)
                .build()
            );

    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
              ApiResponse.<UploadImageRequest>builder()
              .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
              .message("Lỗi khi tải ảnh lên: " + e.getMessage())
              .build()
        );
      }
    }

    private boolean isImage(String contentType) {
        return contentType.equals(MediaType.IMAGE_JPEG_VALUE) ||
                contentType.equals(MediaType.IMAGE_PNG_VALUE) ||
                contentType.equals(MediaType.IMAGE_GIF_VALUE);
    }

    public ResponseEntity<?> downloadImage(String filename) {
      try {
          // Xác định đường dẫn file
          Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
          Resource resource = new UrlResource(filePath.toUri());

          // Kiểm tra xem file có tồn tại và có thể đọc
          if (!resource.exists() || !resource.isReadable()) {
              throw new BadCredentialsException("File not found: " + filename);
          }

          // Lấy kiểu nội dung của file
          String contentType = Files.probeContentType(filePath);
          if (contentType == null) {
              contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
          }

          // Đặt "Content-Disposition" để hiển thị hoặc tải xuống
          return ResponseEntity.ok()
                  .contentType(MediaType.parseMediaType(contentType))
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                  .body(resource);

      } catch (MalformedURLException e) {
          throw new BadCredentialsException("Malformed URL: " + e.getMessage());
      } catch (IOException e) {
          throw new BadCredentialsException("Error reading file: " + e.getMessage());
      }
    }

    public ResponseEntity<?> deleteImageFile(String filename) {
      try {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
  
        // Kiểm tra nếu file tồn tại
        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                  ApiResponse.builder()
                  .status(HttpStatus.NOT_FOUND.value())
                  .message("File không tồn tại: " + filename)
                  .build()
            );
        }
  
        // Xóa file
        Files.delete(filePath);
          return ResponseEntity.ok(
              ApiResponse.builder()
              .status(HttpStatus.OK.value())
              .message("Xóa file thành công: " + filename)
              .build()
        );
  
      } catch (IOException e) {
          throw new BadCredentialsException("Lỗi khi xóa file: " + e.getMessage());
      }
    }
}
