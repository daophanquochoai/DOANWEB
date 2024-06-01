package com.nhom29.Model.ERD;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThongBao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "thongbao")
    private List<ThongBao_ThongTin> thongBaoThongTin = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn( name = "BaiDangId", nullable = false)
    private BaiDang baidang;
    @Column( columnDefinition = "NTEXT", nullable = false)
    private String noidung;
    @Column( nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime thoigiantao;
}
