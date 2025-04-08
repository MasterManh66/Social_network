package com.social_luvina.social_dev8.modules.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.social_luvina.social_dev8.modules.models.dto.request.LikeRequest;
import com.social_luvina.social_dev8.modules.models.dto.response.ApiResponse;
import com.social_luvina.social_dev8.modules.models.dto.response.LikeResponse;
import com.social_luvina.social_dev8.modules.services.interfaces.LikeServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/like")
public class LikeController {
  @Autowired
  private LikeServiceInterface likeService;

  @Operation(summary = "Create Like", description = "Create Like")
  @PostMapping("/create")
  public ResponseEntity<ApiResponse<LikeResponse>> createLike(Authentication authentication, @Validated @RequestBody LikeRequest request) {
        return likeService.createLike(authentication, request.getPostId(), request);
  }

  @Operation(summary = "Delete Like", description = "Delete Like")
  @DeleteMapping("/delete/{postId}")
  public ResponseEntity<ApiResponse<Void>> deleteLike(Authentication authentication, @PathVariable("postId") long postId){
        return likeService.unLike(authentication,postId);
  }

  @Operation(summary = "User's List Like", description = "User's List Like")
  @GetMapping("/listLike")
  public ResponseEntity<ApiResponse<List<LikeResponse>>> getListLike(Authentication authentication){
      return likeService.getListLike(authentication);
  }
}
