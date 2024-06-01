package com.nhom29.Service.Impl;

import com.nhom29.Model.ERD.TaiKhoan;
import com.nhom29.Repository.TaiKhoanRepository;
import com.nhom29.Service.Inter.TaiKhoanInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaiKhoanImpl implements TaiKhoanInter {
    private final TaiKhoanRepository taiKhoanRepo;

    @Override
    public Boolean taiKhoanTrung(String username) {
        Optional<TaiKhoan> taiKhoan = taiKhoanRepo.findById(username);
        if( taiKhoan.isEmpty()) return true;
        return false;
    }
}
