package com.social_luvina.social_dev8.modules.models.dto.request;

import com.social_luvina.social_dev8.modules.models.enums.PostStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
  @Size(max = 255,message = "Tiêu đề không vượt quá 255 ký tự")
  private String title;

  @Size(max = 10000,message = "Nội dung không vượt quá 10,000 ký tự")
  private String content;

  private PostStatus postStatus;

  @Size(max = 5, message = "Tối đa 5 ảnh!")
  private List<String> images;
}
