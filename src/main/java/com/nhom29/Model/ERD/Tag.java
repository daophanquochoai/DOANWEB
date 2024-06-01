package com.nhom29.Model.ERD;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String TenTag;

    @ManyToMany(mappedBy = "tag", fetch = FetchType.EAGER)
    private Set<BaiDang> baiDang = new HashSet<>();
    @Column( columnDefinition = "NTEXT")
    private String noidung;
    @Column(columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime thoigiantao;
}
