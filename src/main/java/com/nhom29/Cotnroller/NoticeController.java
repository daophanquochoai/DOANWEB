package com.nhom29.Cotnroller;

import com.nhom29.DTO.NoticeMessage;
import com.nhom29.DTO.NoticeMessageReceive;
import com.nhom29.Model.ERD.BaiDang;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Service.Inter.BaiDangInter;
import com.nhom29.Service.Inter.ThongBaoInter;
import com.nhom29.Service.Inter.ThongTinInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequiredArgsConstructor
public class NoticeController {
    private final ThongBaoInter thongBaoInter;
    private final ThongTinInter thongTinInter;
    private final BaiDangInter baiDangInter;
    private final RedisTemplate redisTemplate;
    @MessageMapping("/comment")
    @SendTo("/notice/all")
    public NoticeMessageReceive sendNotice(@Payload NoticeMessage noticeMessage){
        Optional<BaiDang> baiDang = baiDangInter.layChiTietBaiDang( noticeMessage.getBaidang());
        Optional<ThongTin> thongTin = thongTinInter.layThongTin(noticeMessage.getNguoicomment());
        if( baiDang.isEmpty() || thongTin.isEmpty() ) throw  new RuntimeException("noticeMessage");
        thongBaoInter.taoThongBaoTheoBaiDang(noticeMessage.getBaidang(), noticeMessage.getNguoicomment());
        baiDang = baiDangInter.layChiTietBaiDang( noticeMessage.getBaidang());
        System.out.println("======================================================");
        System.out.println(baiDang.get().getThongBaoList().size());
        System.out.println("======================================================");
        redisTemplate.opsForValue().set(HomeController.KEY_BD + ":" + baiDang.get().getId(), baiDang.get(), 4, TimeUnit.HOURS);
        return new NoticeMessageReceive(baiDang.get().getId(), baiDang.get().getThongTin().getId(), thongTin.get().getTen(), noticeMessage.getNguoicomment(),  LocalDateTime.now());
    }
}
