package com.nhom29.Redis.Imple;

import com.nhom29.Model.ERD.Report;
import com.nhom29.Redis.Inter.ReportInter;
import com.nhom29.Repository.BaiDangRepository;
import com.nhom29.Repository.BinhLuanRepository;
import com.nhom29.Repository.TagRepository;
import com.nhom29.Repository.ThongTinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class ReportImpl implements ReportInter {
    private final BaiDangRepository baiDangRepo;
    private final TagRepository tagRepo;
    private final ThongTinRepository thongTinRepo;
    private final BinhLuanRepository binhLuanRepo;
    private final RedisTemplate redisTemplate;
    @Override
    public void updateReport() {
        Report report = new Report();
        report.setBaidang(baiDangRepo.getNumber());
        report.setUser(thongTinRepo.getNumber());
        report.setTag(tagRepo.getNumber());
        report.setBinhluan(binhLuanRepo.getNumber());
        report.setBaidang_cobl(baiDangRepo.getNumberHaveAnswer());
        report.setBaidang_khongbl(baiDangRepo.getNumberNoAnswer());
        redisTemplate.opsForValue().set("REPORT", report, 30, TimeUnit.DAYS);
    }
}
