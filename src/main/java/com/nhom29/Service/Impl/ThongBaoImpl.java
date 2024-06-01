package com.nhom29.Service.Impl;

import com.nhom29.Model.ERD.BaiDang;
import com.nhom29.Model.ERD.ThongBao;
import com.nhom29.Model.ERD.ThongBao_ThongTin;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Repository.BaiDangRepository;
import com.nhom29.Repository.ThongBaoRepository;
import com.nhom29.Repository.ThongBao_ThongTinRepository;
import com.nhom29.Repository.ThongTinRepository;
import com.nhom29.Service.Inter.ThongBaoInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThongBaoImpl implements ThongBaoInter {
    private final BaiDangRepository baiDangRepo;
    private final ThongBaoRepository thongBaoRepo;
    private final ThongTinRepository thongTinRepo;
    private final ThongBao_ThongTinRepository thongBaoThongTinRepo;

    @Override
    public List<ThongBao_ThongTin> layThongBaoTheoNguoi(Long id) {
        return thongBaoThongTinRepo.layThongBaoTheoNguoi(id);
    }

    @Override
    public List<ThongBao_ThongTin> layThongBaoTheoNguoiVaTrangThai(Long id, Boolean status) {
        return thongBaoThongTinRepo.layThongBaoTheoNguoiVaStatus(id, status);
    }

    @Override
    public void taoThongBaoTheoBaiDang(Long baiDangId, long nguoiCommentId) {
        Optional<BaiDang> baidang = baiDangRepo.findById(baiDangId);
        if( baidang.isEmpty() ) throw new RuntimeException("bai dang khong ton tai");
        Optional<ThongTin> nguoicomment = thongTinRepo.findById(nguoiCommentId);
        if( nguoicomment.isEmpty() ) throw new RuntimeException("nguoi comment khong ton tai");
        ThongBao thongbao = new ThongBao();
        thongbao.setBaidang(baidang.get());
        thongbao.setThoigiantao(LocalDateTime.now());
        ThongBao_ThongTin thongBaoThongTin = new ThongBao_ThongTin();
        thongBaoThongTin.setStatus(false);
        baidang.get().getLuu().stream().forEach( t -> {
            if( t.getId() != nguoiCommentId ) {
                thongbao.setNoidung("Bài đăng bạn theo dõi đã có " + nguoicomment.get().getTen() + " bình luận");
                thongBaoThongTin.setThongbao(thongbao);
                thongBaoThongTin.setThongTin(t);
                System.out.println("====================================");
                log.info("{}",thongBaoThongTinRepo.save(thongBaoThongTin).getThongTin().getTen());
                System.out.println("====================================");
            }
        });
        if( nguoiCommentId != baidang.get().getThongTin().getId()) {
            thongbao.setNoidung("Bài đăng của bạn đã có " + nguoicomment.get().getTen() + " bình luận");
            thongBaoThongTin.setThongbao(thongbao);
            thongBaoThongTin.setThongTin(baidang.get().getThongTin());
            System.out.println("====================================");
            log.info("{}", thongBaoThongTinRepo.save(thongBaoThongTin).getThongTin().getTen());
            System.out.println("====================================");
        }
    }

    @Override
    public void docThongBaoTheoNguoi(Long nguoiId, Long BaiDangId) {
        Optional<ThongTin> thongtin = thongTinRepo.findById(nguoiId);
        if( thongtin.isEmpty() ) throw new RuntimeException("Not found thongtin");
        thongtin.get().getThongBaoThongTin().forEach(t -> {
            if(t.getThongbao().getBaidang().getId().equals(BaiDangId)){
                t.setStatus(true);
            }
            thongBaoThongTinRepo.save(t);
        });
//        thongTinRepo.save(thongtin.get());
    }

    @Override
    public long thongBaoChuaXem(Long id) {
        List<ThongBao_ThongTin> thongBaoThongTins = layThongBaoTheoNguoi(id);
        return thongBaoThongTins.stream().filter( s -> !s.getStatus()).count();
    }
}
