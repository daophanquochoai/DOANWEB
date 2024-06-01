package com.nhom29.Service.Impl;

import com.nhom29.Model.ERD.UyQuyen;
import com.nhom29.Repository.UyQuyenRepository;
import com.nhom29.Service.Inter.UyQuyenInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UyQuyenImpl implements UyQuyenInter {
    private final UyQuyenRepository uyQuyenRepo;
    @Override
    public Optional<UyQuyen> layQuyenUser(String quyen) {
        return uyQuyenRepo.findByQuyen(quyen);
    }
}
