package com.nhom29.Service.Inter;

import com.nhom29.Model.ERD.BinhLuan;
import org.springframework.data.domain.Page;

public interface BinhLuanInter {
    Page<BinhLuan> layBinhLuanTheoBaiDangVaPhanTrang( Long id, Integer soluong, String sort);
    BinhLuan luuBinhLuan(BinhLuan binhLuan);
}
