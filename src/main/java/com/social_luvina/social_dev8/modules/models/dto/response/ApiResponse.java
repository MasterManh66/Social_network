package com.social_luvina.social_dev8.modules.models.dto.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
// import com.social_luvina.social_dev8.modules.models.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  @Builder.Default
  private int status = HttpStatus.OK.value();

  @Builder.Default
  private String message = "success";
    
  private T data;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();
}
