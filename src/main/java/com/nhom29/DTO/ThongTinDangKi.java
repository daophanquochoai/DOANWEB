package com.nhom29.DTO;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongTinDangKi {
    private MultipartFile avatar;
    @Length( min = 5, message = " * Họ và tên quá ngắn")
    private String hovaten;
    @Length( min = 10, max = 10, message = " * Số điện thoại đúng 10 số")
    @Pattern(regexp = "^(?:\\+?84|0)(?:\\d{9,10})$\n", message = "Số điện thoại chưa đúng định dạng")
    private String sodienthoai;
    @Pattern(regexp = "^[\\w+-.]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$", message = " * Email chưa đúng định dạng")
    private String email;
    @Length( min = 5, message = " * Trường học quá ngắn")
    private String truonghoc;
    @Length( min = 10, message = " * Bạn chia sẻ về mình nhiều hơn đi !!!")
    private String gioithieuveminh;
    @Length( min = 5, message = " * Tài khoản quá ngắn")
    private String taikhoan;
    private String matkhau;
}
