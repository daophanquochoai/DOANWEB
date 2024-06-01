package com.nhom29.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongTinDangKi {
    private MultipartFile avatar;
    private String hovaten;
    private String sodienthoai;
    private String email;
    private String truonghoc;
    private String gioithieuveminh;
    private String taikhoan;
    private String matkhau;
}
