package com.nhom29.Model.ERD;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan_ThongTin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne(mappedBy = "taiKhoanThongTin")
    private ThongTin thongtin;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn( name = "TaiKhoan_Id")
    private TaiKhoan taiKhoan;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn( name = "UyQuyen_ID", nullable = false)
    private UyQuyen uyQuyen ;
}
