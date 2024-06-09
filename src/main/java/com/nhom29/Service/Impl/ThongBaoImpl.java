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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void taoThongBaoTheoBaiDang(Long baiDangId, long nguoiCommentId) {
        Optional<BaiDang> baidangOpt = baiDangRepo.findById(baiDangId);
        if (baidangOpt.isEmpty()) {
            throw new RuntimeException("bai dang khong ton tai");
        }

        Optional<ThongTin> nguoicommentOpt = thongTinRepo.findById(nguoiCommentId);
        if (nguoicommentOpt.isEmpty()) {
            throw new RuntimeException("nguoi comment khong ton tai");
        }

        BaiDang baidang = baidangOpt.get();
        ThongTin nguoicomment = nguoicommentOpt.get();

        ThongBao thongbao = new ThongBao();
        thongbao.setBaidang(baidang);
        thongbao.setThoigiantao(LocalDateTime.now());
        baidang.getLuu().stream().forEach(t -> {
            if (t.getId() != nguoiCommentId) {
                ThongBao_ThongTin thongBaoThongTin = new ThongBao_ThongTin();
                thongbao.setNoidung("Bài đăng bạn theo dõi đã có " + nguoicomment.getTen() + " bình luận");
                ThongBao temp = thongBaoRepo.save(thongbao);
                thongBaoThongTin.setThongbao(temp);
                thongBaoThongTin.setThongTin(t);
                thongBaoThongTin.setStatus(false);
                thongBaoThongTinRepo.save(thongBaoThongTin);
            }
        });

        if (nguoiCommentId != baidang.getThongTin().getId()) {
            System.out.println("================================");
            System.out.println("Bài đăng của bạn đã có " + nguoicomment.getTen() + " bình luận");
            System.out.println("================================");
            ThongBao_ThongTin thongBaoThongTin = new ThongBao_ThongTin();
            thongbao.setNoidung("Bài đăng của bạn đã có " + nguoicomment.getTen() + " bình luận");
            ThongBao temp = thongBaoRepo.save(thongbao);
            thongBaoThongTin.setThongbao(temp);
            thongBaoThongTin.setThongTin(baidang.getThongTin());
            thongBaoThongTin.setStatus(false);
            thongBaoThongTinRepo.save(thongBaoThongTin);
        }
    }


    @Override
    public void docThongBaoTheoNguoi(Long nguoiId, Long BaiDangId) {
        System.out.println(nguoiId);
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

    @Override
    public void deleteById(Long Id) {
        thongBaoRepo.deleteById(Id);
    }
}
