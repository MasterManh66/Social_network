package com.social_luvina.social_dev8.modules.models.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long userId;
    private List<String> images;
    private long postId;
}
