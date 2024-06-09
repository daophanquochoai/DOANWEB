package com.nhom29.Service.Impl;

import com.nhom29.DTO.Pagination;
import com.nhom29.DTO.pageUsers_ThongTin;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Repository.ThongTinRepository;
import com.nhom29.Service.Inter.CloudinaryInter;
import com.nhom29.Service.Inter.ThongTinInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThongTinImpl implements ThongTinInter {
    private final ThongTinRepository thongTinRepository;
    private final CloudinaryInter cloudinaryInter;

    @Override
    public Optional<ThongTin> layThongTin(Long id) {
        return thongTinRepository.findById(id);
    }

    @Override
    public Optional<ThongTin> layThongTinByUserName(String username) {
        return thongTinRepository.findByUsername(username);
    }

    @Override
    public Optional<ThongTin> layThongTInByEmail(String email) {
        return thongTinRepository.findByEmail(email);
    }

    @Override
    public ThongTin updateThongTin(ThongTin thongTinGoc, MultipartFile multipartFile, ThongTin thongTin) throws IOException {
        if( !multipartFile.isEmpty()){
            if( !multipartFile.isEmpty() ){
                String url = cloudinaryInter.uploadFile(multipartFile);
                thongTinGoc.setAnhDaiDien(url);
            }
        }
        thongTinGoc.setHo(thongTin.getHo());
        thongTinGoc.setTen(thongTin.getTen());
        thongTinGoc.setSdt(thongTin.getSdt());
        thongTinGoc.setTruong(thongTin.getTruong());
        thongTinGoc.setEmail(thongTin.getEmail());
        thongTinGoc.setGioiThieu(thongTin.getGioiThieu());
        return thongTinRepository.save(thongTinGoc);
    }

    @Override
    public void updateMK(ThongTin thongTin) {
        thongTinRepository.save(thongTin);
    }

    @Override
    public void luuThongTin(ThongTin thongTin) {
        thongTinRepository.save(thongTin);
    }

    @Override
    public pageUsers_ThongTin layThongTinTheoPageVaQ(Integer page, String q) {
        PageRequest pageRequest = PageRequest.of(page, 8);
        Page<ThongTin> list;
        if (q == null || q.isEmpty()) {
            q = "";
        }
        list = thongTinRepository.findAllUserWithQ(q, pageRequest);
        return new pageUsers_ThongTin(list, new Pagination(list.getTotalPages(),page+1));
    }

    @Override
    public Optional<ThongTin> layThongTinBySdt(String sdt) {
        return thongTinRepository.findBySdt(sdt);
    }
}
