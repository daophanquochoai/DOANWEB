package com.nhom29.Service.Impl;

import com.nhom29.Model.ERD.BinhLuan;
import com.nhom29.Repository.BinhLuanRepository;
import com.nhom29.Service.Inter.BinhLuanInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinhLuanImpl implements BinhLuanInter {
    private final BinhLuanRepository binhLuanRepo;
    @Override
    public Page<BinhLuan> layBinhLuanTheoBaiDangVaPhanTrang(Long id, Integer soluong, String sort) {
        List<BinhLuan> danhSachBinhLuan;
        System.out.println("===========================");
        log.info("{}", sort);
        System.out.println("===========================");
        if( sort.equals("thoigian")){
            danhSachBinhLuan = binhLuanRepo.layBinhLuanTheoBaiDangSortThoiGian(id);
        }else{
            danhSachBinhLuan = binhLuanRepo.layBinhLuanTheoBaiDang(id);
        }

        // Kiểm tra nếu số lượng yêu cầu lớn hơn số lượng thực sự có, chỉ lấy số lượng thực sự có
        int soLuongThatSuCo = Math.min(soluong, danhSachBinhLuan.size());

        // Tạo một trang mới chỉ chứa số lượng bình luận được yêu cầu
        if (soLuongThatSuCo > 0) {
            Page<BinhLuan> page = new PageImpl<>(danhSachBinhLuan.subList(0, soLuongThatSuCo), PageRequest.of(0, soLuongThatSuCo), danhSachBinhLuan.size());
            return page;
        } else {
            return Page.empty();
        }
    }



    @Override
    public BinhLuan luuBinhLuan(BinhLuan binhLuan) {
        return binhLuanRepo.save(binhLuan);
    }
}
