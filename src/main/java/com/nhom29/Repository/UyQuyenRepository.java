package com.nhom29.Repository;

import com.nhom29.Model.ERD.UyQuyen;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UyQuyenRepository extends JpaRepository<UyQuyen, Long> {
    @Query("SELECT u FROM UyQuyen u WHERE u.Quyen = :quyen")
    Optional<UyQuyen> findByQuyen(@Param("quyen") String quyen);
}

