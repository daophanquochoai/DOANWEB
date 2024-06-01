package com.nhom29.Service.Inter;

import com.nhom29.Model.ERD.ThongBao_ThongTin;

import java.util.List;

public interface ThongBaoInter {
    List<ThongBao_ThongTin> layThongBaoTheoNguoi(Long id);
    List<ThongBao_ThongTin> layThongBaoTheoNguoiVaTrangThai( Long id, Boolean status);
    void taoThongBaoTheoBaiDang( Long baiDangId, long nguoiCommentId);
    void docThongBaoTheoNguoi(Long nguoiId, Long BaiDangId);
    long thongBaoChuaXem( Long id);
}
