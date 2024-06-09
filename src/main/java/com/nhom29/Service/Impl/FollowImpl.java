package com.nhom29.Service.Impl;

import com.nhom29.Cotnroller.HomeController;
import com.nhom29.Model.ERD.BaiDang;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Repository.BaiDangRepository;
import com.nhom29.Repository.ThongTinRepository;
import com.nhom29.Service.Inter.FollowInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowImpl implements FollowInter {
    private final BaiDangRepository baiDangRepo;
    private final ThongTinRepository thongTinRepo;
    private final RedisTemplate redisTemplate;

    @Override
    public void follow(Long baidangId, Long thongtinId) {
        Optional<BaiDang> baiDang = baiDangRepo.findById(baidangId);
        if( baiDang.isEmpty() ) throw new RuntimeException("Not found baidang");
        Optional<ThongTin> thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.isEmpty() ) throw new RuntimeException("Not found thongtin");
        baiDang.get().getLuu().add(thongTin.get());
        BaiDang b = baiDangRepo.save(baiDang.get());
        redisTemplate.opsForValue().set( HomeController.KEY_BD + ":" + baidangId, b, 4, TimeUnit.HOURS);
        thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.get().getTaiKhoanThongTin() == null){
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getId(),thongTin.get(), 4, TimeUnit.HOURS );
        }else{
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getTaiKhoanThongTin().getTaiKhoan().getUsername(),thongTin.get(), 4, TimeUnit.HOURS );
        }
    }

    @Override
    public void unfollow(Long baidangId, Long thongtinId) {
        Optional<BaiDang> baiDang = baiDangRepo.findById(baidangId);
        if( baiDang.isEmpty() ) throw new RuntimeException("Not found baidang");
        Optional<ThongTin> thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.isEmpty() ) throw new RuntimeException("Not found thongtin");
        baiDang.get().getLuu().add(thongTin.get());
        baiDang.get().setLuu(baiDang.get().getLuu().stream().filter(t -> t.getId() != thongtinId).collect(Collectors.toList()));
        BaiDang b =baiDangRepo.save(baiDang.get());
        redisTemplate.opsForValue().set( HomeController.KEY_BD + ":" + baidangId, b, 4, TimeUnit.HOURS);
        thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.get().getTaiKhoanThongTin() == null){
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getId(),thongTin.get(), 4, TimeUnit.HOURS );
        }else{
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getTaiKhoanThongTin().getTaiKhoan().getUsername(),thongTin.get(), 4, TimeUnit.HOURS );
        }
    }

    @Override
    public void like(Long baidangId, Long thongtinId) {
        Optional<BaiDang> baiDang = baiDangRepo.findById(baidangId);
        if( baiDang.isEmpty() ) throw new RuntimeException("Not found baidang");
        Optional<ThongTin> thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.isEmpty() ) throw new RuntimeException("Not found thongtin");
        baiDang.get().getLike().add(thongTin.get());
        BaiDang b = baiDangRepo.save(baiDang.get());
        redisTemplate.opsForValue().set( HomeController.KEY_BD + ":" + baidangId, b, 4, TimeUnit.HOURS);
        thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.get().getTaiKhoanThongTin() == null){
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getId(),thongTin.get(), 4, TimeUnit.HOURS );
        }else{
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getTaiKhoanThongTin().getTaiKhoan().getUsername(),thongTin.get(), 4, TimeUnit.HOURS );
        }
    }

    @Override
    public void unlike(Long baidangId, Long thongtinId) {
        Optional<BaiDang> baiDang = baiDangRepo.findById(baidangId);
        if( baiDang.isEmpty() ) throw new RuntimeException("Not found baidang");
        Optional<ThongTin> thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.isEmpty() ) throw new RuntimeException("Not found thongtin");
        baiDang.get().getLike().add(thongTin.get());
        baiDang.get().setLike(baiDang.get().getLike().stream().filter(t -> t.getId() != thongtinId).collect(Collectors.toList()));
        BaiDang b =baiDangRepo.save(baiDang.get());
        redisTemplate.opsForValue().set( HomeController.KEY_BD + ":" + baidangId, b, 4, TimeUnit.HOURS);
        thongTin = thongTinRepo.findById(thongtinId);
        if( thongTin.get().getTaiKhoanThongTin() == null){
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getId(),thongTin.get(), 4, TimeUnit.HOURS );
        }else{
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin.get().getTaiKhoanThongTin().getTaiKhoan().getUsername(),thongTin.get(), 4, TimeUnit.HOURS );
        }
    }
}
