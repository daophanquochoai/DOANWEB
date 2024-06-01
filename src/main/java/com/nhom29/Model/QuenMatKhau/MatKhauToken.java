package com.nhom29.Model.QuenMatKhau;

import com.nhom29.Model.ERD.ThongTin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatKhauToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String token;
    @OneToOne(targetEntity = ThongTin.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private ThongTin thongTin;
    private LocalDateTime thoihan;
    @Column(columnDefinition = "BIT")
    private Boolean aceept;
}
