package com.nhom29.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NoticeMessageReceive {
    private Long baiDangId;
    private Long nguoiDangId;
    private String tenNguoiComment;
    private Long nguoiCommentId;
    private LocalDateTime thoiGian;
}
