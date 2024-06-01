package com.nhom29.Repository;

import com.nhom29.Model.ERD.ThongBao_ThongTin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThongBao_ThongTinRepository extends JpaRepository<ThongBao_ThongTin, Long> {
    @Query("select t from ThongBao_ThongTin t where t.thongTin.id = :id order by t.thongbao.thoigiantao desc ")
    List<ThongBao_ThongTin> layThongBaoTheoNguoi( Long id);

    @Query("select t from ThongBao_ThongTin t where t.status = :status and t.thongTin.id = :id")
    List<ThongBao_ThongTin> layThongBaoTheoNguoiVaStatus( Long id, Boolean status);
}
