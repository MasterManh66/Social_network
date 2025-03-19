package com.social_luvina.social_dev8.modules.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.social_luvina.social_dev8.modules.models.dto.response.ErrorResource;

@ControllerAdvice
public class GlobalException {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidException(MethodArgumentNotValidException exception){

    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    ErrorResource errorResource = new ErrorResource("Có vấn đề xảy ra trong quá trình xử lý", errors);
    return new ResponseEntity<>(errorResource, HttpStatus.UNPROCESSABLE_ENTITY);

  }
}
