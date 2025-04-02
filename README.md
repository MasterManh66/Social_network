
Mã lỗi HttpStatus

Một số HTTP được sử dụng trong bài 
* Mã lỗi 2xx : Success ( Thành công )
        200  | OK           | Trả về Dữ liệu request thành công           | Hầu hết các request GET, POST, PUT thành công đều trả về
        201  | Created      | Dữ liệu được tạo thành công                 | Khi tạo resource mới ( VD: tạo thành công user, post, like, comment, ...)
        204  | No Content   | Thành công nhưng không có Nội dung trả về   | Khi request DELETE or PUT không cần trả về ( void )


* Mã lỗi 4xx : Client Error
        400  | Bad Request   | Request sai cú pháp hoặc không hợp lệ      | Khi Dữ liệu request bị thiếu hoặc sai định dạng
        401  | Unauthorized  | Chưa xác thực                              | Khi user chưa đăng nhập hoặc token không hợp lệ
        403  | Forbidden     | Đã xác thực nhưng không có quyền           | Khi user không có quyền truy cập tài nguyên
        404  | Not Found     | Không tìm thấy tài nguyên                  | Khi API hoặc file không tồn tại
        409  | Conflict      | Có xung đột trong request                  | Khi tài nguyên trùng ( VD: email đã tồn tại ...)
        422  | Unprocessable Entity |  Request đúng cú pháp nhưng dữ liệu không hợp lệ | Khi validation dữ liệu thất bại (VD: email sai định dạng)

* Mã lỗi 5xx : Server error
        500  | 	Internal Server Error   | Lỗi server không xác định       | 	Khi có lỗi trong backend hoặc exception không xử lý được