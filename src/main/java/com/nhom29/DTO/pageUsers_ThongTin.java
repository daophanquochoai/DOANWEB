package com.nhom29.DTO;

import com.nhom29.Model.ERD.ThongTin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class pageUsers_ThongTin {
    Page<ThongTin> thongTins;
    Pagination pagination;
}
