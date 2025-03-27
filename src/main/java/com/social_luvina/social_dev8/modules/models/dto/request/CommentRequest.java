package com.social_luvina.social_dev8.modules.models.dto.request;

import java.util.List;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    @NotNull(message = "Post ID is required")
    private Long postId;

    @Size(max = 5, message = "Tối đa 5 ảnh!")
    private List<String> images;
  
    @Size(max = 10000, message = "Nội dung không vượt quá 10,000 ký tự")
    private String content;
}
