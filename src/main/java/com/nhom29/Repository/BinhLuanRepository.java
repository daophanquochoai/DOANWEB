package com.nhom29.Repository;

import com.nhom29.Model.ERD.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {
    @Query("select b from BinhLuan b where b.baidang.id = :id")
    List<BinhLuan> layBinhLuanTheoBaiDang(Long id);
    @Query("select b from BinhLuan b where b.baidang.id = :id order by b.date desc ")
    List<BinhLuan> layBinhLuanTheoBaiDangSortThoiGian(Long id);
    @Query("select count(b) from BinhLuan b")
    Long getNumber();
}
