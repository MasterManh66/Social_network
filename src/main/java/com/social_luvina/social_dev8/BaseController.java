// package com.social_luvina.social_dev8;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.web.bind.annotation.GetMapping;



// @RestController
// @RequestMapping("/api")
// public class BaseController {
  
//   private final JdbcTemplate jdbcTemplate;

//   public BaseController(JdbcTemplate jdbcTemplate){
//     this.jdbcTemplate = jdbcTemplate;
//   }

//   @GetMapping("test")
//   public String test() {

//     String sql = "CREATE TABLE IF NOT EXISTS test_table ("
//       +" id INT AUTO_INCREMENT PRIMARY KEY, " +
//       "name VARCHAR(250) NOT NULL" +
//     ")";

//     jdbcTemplate.execute(sql);

//     return "Đã tạo thành công";
//   }
  
// }
