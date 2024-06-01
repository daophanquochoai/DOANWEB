package com.nhom29.Service.Inter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryInter {
    String uploadFile(MultipartFile multipartFile) throws IOException;
}
