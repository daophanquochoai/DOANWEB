package com.nhom29.Service.Inter;

import com.nhom29.DTO.pageUsers_ThongTin;
import com.nhom29.Model.ERD.ThongTin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ThongTinInter {
    Optional<ThongTin> layThongTin(Long id);
    Optional<ThongTin> layThongTinByUserName( String username);
    Optional<ThongTin> layThongTInByEmail(String email);
    ThongTin updateThongTin(ThongTin thongTinGoc, MultipartFile multipartFile,ThongTin thongTin) throws IOException;
    void updateMK(ThongTin thongTin);
    void luuThongTin( ThongTin thongTin);
    pageUsers_ThongTin layThongTinTheoPageVaQ(Integer page, String q);
    Optional<ThongTin> layThongTinBySdt(String sdt);
}
