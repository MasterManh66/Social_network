package com.social_luvina.social_dev8.modules.models.dto.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadImageRequest {
  private List<String> images;
}
