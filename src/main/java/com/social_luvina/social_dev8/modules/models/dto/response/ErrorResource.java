package com.social_luvina.social_dev8.modules.models.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResource {
  
  private String message;
  private Map<String, String> errors;


}
