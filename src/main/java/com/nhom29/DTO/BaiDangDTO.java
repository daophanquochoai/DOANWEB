package com.nhom29.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiDangDTO {
    private Long id;
    private String TieuDe;
    private String NoiDung;
    private MultipartFile[] files;
    private List<String> tags;
}
