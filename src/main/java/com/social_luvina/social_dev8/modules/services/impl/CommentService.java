package com.social_luvina.social_dev8.modules.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.social_luvina.social_dev8.modules.exception.CustomException;
import com.social_luvina.social_dev8.modules.models.dto.request.CommentRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.CommentResponse;
import com.social_luvina.social_dev8.modules.models.entities.Post;
import com.social_luvina.social_dev8.modules.models.entities.User;
import com.social_luvina.social_dev8.modules.models.entities.Comment;
import com.social_luvina.social_dev8.modules.repositories.CommentRepository;
import com.social_luvina.social_dev8.modules.repositories.PostRepository;
import com.social_luvina.social_dev8.modules.repositories.UserRepository;
import com.social_luvina.social_dev8.modules.services.interfaces.CommentServiceInterface;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentService implements CommentServiceInterface {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageService imageService;

    private User getAuthenticatedUser(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new BadCredentialsException("User không tồn tại"));
    }

    private Post getPost(long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));
    }

    private Comment getComment(long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException("Comment không tồn tại", HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(Authentication authentication, long postId, CommentRequest request) {
        if ((request.getImages() == null || request.getImages().isEmpty()) && (request.getContent() == null || request.getContent().isEmpty())) {
            throw new CustomException("Images hoặc content bị trống", HttpStatus.BAD_REQUEST);
        }

        User user = getAuthenticatedUser(authentication);
        Post post = getPost(postId);

        List<String> imagePaths = request.getImages() != null ? imageService.saveImage(request.getImages()) : null;

        Comment comment = Comment.builder()
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .images(imagePaths)
                .user(user)
                .post(post)
                .build();

        commentRepository.save(comment);

        CommentResponse commentResponse = new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
            comment.getUser().getId(),
            comment.getImages(),
            comment.getPost().getId()
        );

        return ResponseEntity.ok(
            ApiResponse.<CommentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Tạo bình luận thành công!")
                .data(commentResponse)
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<CommentResponse>> editComment(Authentication authentication, long commentId, CommentRequest request){
        if ((request.getImages() == null || request.getImages().isEmpty()) && request.getContent() == null) {
            throw new CustomException("Images or Content is required", HttpStatus.BAD_REQUEST);
        }

        User user = getAuthenticatedUser(authentication);
        Comment comment = getComment(commentId);
      
        if (comment.getUser().getId() != user.getId()) {
            throw new CustomException("Bạn không có quyền chỉnh sửa comment này.", HttpStatus.FORBIDDEN);
        }

        boolean isUpdated = false;

        if (request.getContent() != null && !request.getContent().equals(comment.getContent())) {
            comment.setContent(request.getContent());
            isUpdated = true;
        }

        comment.setUpdatedAt(LocalDateTime.now());
        
        if ((request.getImages() != null && !request.getImages().isEmpty()) && request.getImages().equals(comment.getImages())) {
            comment.setImages(request.getImages());
            isUpdated = true;
        }
      
        if (isUpdated){
            commentRepository.save(comment);
        }
      
        CommentResponse commentResponse = new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
            comment.getUser().getId(),
            comment.getImages(),
            comment.getPost().getId()
        );
      
        return ResponseEntity.ok(
            ApiResponse.<CommentResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Bình luận đã sửa đổi!")
                .data(commentResponse)
                .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> deleteComment(Authentication authentication, long commentId){
        Comment comment = getComment(commentId);
        User user = getAuthenticatedUser(authentication);

        if (comment.getUser().getId() != user.getId()) {
            throw new CustomException("Bạn không có quyền xoá comment này.", HttpStatus.FORBIDDEN);
        }

        if (comment.getImages() != null){
            for(String path : comment.getImages()){
                String sanitizedPath = path.replace("/", "");
                imageService.deleteImageFile(sanitizedPath);
            }
        }

        commentRepository.delete(comment);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
            .message("Delete comment completed")
            .build();
        return ResponseEntity.ok(apiResponse);
    }
}
