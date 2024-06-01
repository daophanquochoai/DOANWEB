package com.nhom29.DTO;

import com.nhom29.Model.ERD.BaiDang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class pageQuestion_BaiDang {
    Page<BaiDang> baiDangPage;
    Integer totalPage;
}
