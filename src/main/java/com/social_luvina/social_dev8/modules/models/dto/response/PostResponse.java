package com.social_luvina.social_dev8.modules.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.social_luvina.social_dev8.modules.models.entities.Post;
import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {
  private long id;
  private long userId;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private PostStatus postStatus;
  private List<String> images;

  private Integer likeCount;
  private Integer commentCount;

  public PostResponse(Post post) {
    this.id = post.getId();
    this.userId = post.getUser().getId();
    this.title = post.getTitle();
    this.content = post.getContent();
    this.createdAt = post.getCreatedAt();
    this.postStatus = post.getPostStatus();
    this.images = post.getImages();
  }

  public PostResponse(long id, long userId, String title, String content, LocalDateTime createdAt, List<String> images, Integer likeCount, Integer commentCount) {
    this.id = id;
    this.userId = userId;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.images = images;
    this.likeCount = likeCount;
    this.commentCount = commentCount;
  }
}
