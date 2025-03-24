package com.social_luvina.social_dev8.modules.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.social_luvina.social_dev8.modules.models.entities.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelGenerator {
  public static ByteArrayInputStream generateExcelReport(User user, int postCount, int newFriendCount, int totalLike, int newCommentCount) throws IOException {
    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        Sheet sheet = workbook.createSheet("User Report");

        // Tạo tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Họ và Tên", "Email", "Bài viết", "Bạn bè mới", "Lượt thích", "Bình luận mới"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getHeaderCellStyle(workbook));
        }

        // Thêm dữ liệu
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());
        dataRow.createCell(1).setCellValue(user.getEmail());
        dataRow.createCell(2).setCellValue(postCount);
        dataRow.createCell(3).setCellValue(newFriendCount);
        dataRow.createCell(4).setCellValue(totalLike);
        dataRow.createCell(5).setCellValue(newCommentCount);

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
  }

  private static CellStyle getHeaderCellStyle(Workbook workbook) {
      CellStyle style = workbook.createCellStyle();
      Font font = workbook.createFont();
      font.setBold(true);
      style.setFont(font);
      return style;
  }
}
