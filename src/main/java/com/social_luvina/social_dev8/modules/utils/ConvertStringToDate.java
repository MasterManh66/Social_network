package com.social_luvina.social_dev8.modules.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.security.authentication.BadCredentialsException;

import java.sql.Date;


public class ConvertStringToDate {
  
  public static Date convert(String date) {
    try {
      if (date == null || date.trim().isEmpty()) {
        return null;
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      LocalDate localDate = LocalDate.parse(date, formatter);

      if (!isValidDate(localDate)) {
        throw new BadCredentialsException("The date of birth must be in the past and you must be older than 13 years old");
      }

      return Date.valueOf(localDate);
    } catch (Exception e) {
        throw new BadCredentialsException("Date is not valid!" + e.getMessage());
    }
  }

  private static boolean isValidDate(LocalDate date) {
    LocalDate today = LocalDate.now();
    LocalDate minAgeDate = today.minusYears(13);
    return date.isBefore(today) && date.isBefore(minAgeDate);
  }
}
