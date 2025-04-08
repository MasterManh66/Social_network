package com.social_luvina.social_dev8.modules.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.CommentRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.CommentResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.CommentServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentServiceInterface commentService;

    @Operation(summary = "Create Comment", description = "Create Comment")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(Authentication authentication, @Validated @RequestBody CommentRequest request) {
        return commentService.createComment(authentication, request.getPostId(), request);
    }

    @Operation(summary = "Edit Comment", description = "Edit Comment")
    @PutMapping("/edit/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> editComment(Authentication authentication ,@PathVariable("commentId") long commentId, @Validated @RequestBody CommentRequest request) {
        return commentService.editComment(authentication, commentId, request);
    }

    @Operation(summary = "Delete Comment", description = "Delete Comment")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(Authentication authentication, @PathVariable("commentId") long commentId){
        return commentService.deleteComment(authentication,commentId);
    }

    @Operation(summary = "List Comment", description = "List Comment")
    @GetMapping("/listComment")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentById(Authentication authentication) {
        return commentService.getCommentById(authentication);
    }
    
}
