package com.nhom29.Model.ERD;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ThongTin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(255)", unique = true)
    @Pattern(regexp = "^[\\w+-.]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}$", message = "Email chưa đúng định dạng")
    private String Email;
    @Column(columnDefinition = "nvarchar(100)")
    private String Ho;
    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String Ten;
    @Column(columnDefinition = "nvarchar(255)")
    private String Truong;
    @Column(columnDefinition = "varchar(10)", unique = true)
    @Length(max = 10, min = 10, message = "SDT có 10 kí tự")
    private String Sdt;
    @Column(name = "AnhDaiDien", columnDefinition = "TEXT")
    private String anhDaiDien;
    @Column(columnDefinition = "nvarchar(500)", nullable = true)
    private String GioiThieu;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TaiKhoan_ThongTin")
    private TaiKhoan_ThongTin taiKhoanThongTin;
    @Column(columnDefinition = "varchar(20)")
    private String providerId;
    @ManyToMany(mappedBy = "luu", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private List<BaiDang> baiDang_Luu = new ArrayList<>();
    @ManyToMany(mappedBy = "like", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private List<BaiDang> baiDang_Like = new ArrayList<>();
    @OneToMany(mappedBy = "thongTin", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<BaiDang> baiDangs = new ArrayList<>();
    @OneToMany(mappedBy = "thongTin", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<ThongBao_ThongTin> thongBaoThongTin = new ArrayList<>();
    @OneToMany(mappedBy = "thongTin", fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.DETACH})
    private List<BinhLuan> binhLuans;
}
