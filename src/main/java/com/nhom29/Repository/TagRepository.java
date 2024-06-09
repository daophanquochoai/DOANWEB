package com.nhom29.Repository;

import com.nhom29.Model.ERD.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("select t from Tag t where t.TenTag = :name")
    Optional<Tag> findTagByName(String name);
    @Query("select count(t) from Tag t")
    Long getNumber();

    @Query("select t from Tag t where LOWER(t.TenTag) like LOWER(CONCAT('%', :q, '%')) or t.noidung like LOWER(CONCAT('%', :q, '%')) ")
    Page<Tag> findByQ(String q, PageRequest pageable);
}
