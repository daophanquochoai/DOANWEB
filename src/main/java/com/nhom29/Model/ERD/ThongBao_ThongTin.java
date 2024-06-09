package com.nhom29.Model.ERD;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThongBao_ThongTin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "thongTin")
    private ThongTin thongTin;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thongBao")
    private ThongBao thongbao;
    private Boolean status;
}
