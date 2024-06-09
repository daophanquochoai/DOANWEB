package com.nhom29.Service.Inter;

import com.nhom29.DTO.pageQuestion_BaiDang;
import com.nhom29.Model.ERD.BaiDang;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface BaiDangInter {
    Page<BaiDang> timBaiDangPhanTrang(int offset, int pageSize, String feild);
    @Transactional
    BaiDang saveBaiDang(BaiDang baiDang);
    Integer getNumberPage();
    Optional<BaiDang> layChiTietBaiDang( Long id);
    pageQuestion_BaiDang timBaiDangPhanTrangVaLoc(int offset, int pageSize, String sort, String filter, String[] tagUsed, String q);
    Integer tongBaiDang();
    void deleteQuestion( Long baidang );
    pageQuestion_BaiDang layBaiDangTheoDoi( Long thongtinId, Integer soluong, String q);

}
