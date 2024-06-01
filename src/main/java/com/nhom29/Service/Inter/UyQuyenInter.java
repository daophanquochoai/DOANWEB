package com.nhom29.Service.Inter;

import com.nhom29.Model.ERD.UyQuyen;

import java.util.Optional;

public interface UyQuyenInter {
    Optional<UyQuyen> layQuyenUser( String quyen);
}
