package com.social_luvina.social_dev8.modules.models.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    private Long postId;
    private List<String> images;
  
    @Size(max = 10000, message = "Content must not exceed 2500 words")
    private String content;
}
