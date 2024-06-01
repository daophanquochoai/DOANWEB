package com.nhom29.Model.ERD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report implements Serializable {
    private Long user;
    private Long baidang;
    private Long binhluan;
    private Long tag;
    private Long baidang_cobl;
    private Long baidang_khongbl;
}
