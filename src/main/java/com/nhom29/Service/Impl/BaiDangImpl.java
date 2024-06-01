package com.nhom29.Service.Impl;

import com.nhom29.Cotnroller.HomeController;
import com.nhom29.DTO.pageQuestion_BaiDang;
import com.nhom29.Model.ERD.BaiDang;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Repository.BaiDangRepository;
import com.nhom29.Repository.ThongTinRepository;
import com.nhom29.Service.Inter.BaiDangInter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaiDangImpl implements BaiDangInter {
    private final BaiDangRepository baiDangRepo;
    private final ThongTinRepository thongTinRepo;
    private final RedisTemplate redisTemplate;
    @Override
    public Page<BaiDang> timBaiDangPhanTrang(int offset, int pageSize, String feild) {
        return baiDangRepo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(feild).descending()));

    }

    @Override
    public BaiDang saveBaiDang(BaiDang baiDang) {
        BaiDang baiDang1 =  baiDangRepo.save(baiDang);
        redisTemplate.opsForValue().set(HomeController.KEY + ":" + baiDang1.getThongTin().getId(), thongTinRepo.findById(baiDang1.getThongTin().getId()).get(), 4, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(HomeController.KEY_BD + ":" + baiDang1.getId(), baiDang1, 4, TimeUnit.HOURS);
        return baiDang1;

    }

    @Override
    public Integer getNumberPage() {
        Integer number = baiDangRepo.getNumberPage();
        return  (int) Math.ceil((double) number / (double) HomeController.numberPage) == 0 ? 1 : (int) Math.ceil((double) number / (double) HomeController.numberPage);
    }

    @Override
    public Optional<BaiDang> layChiTietBaiDang(Long id) {
        System.out.println("============================");
        System.out.println("USING DB");
        System.out.println("============================");
        return baiDangRepo.findById(id);
    }

    @Override
    public pageQuestion_BaiDang timBaiDangPhanTrangVaLoc(int offset, int pageSize, String sort, String filter, String[] tagUsed, String q) {
        List<BaiDang> listBaiDang = baiDangRepo.findAll();
        if( sort.equals("newest") ){
            listBaiDang.sort( Comparator.comparing(BaiDang::getThoigiantao).reversed() );
        }
        else if( sort.equals("oldest") ){
            listBaiDang.sort( Comparator.comparing(BaiDang::getThoigiantao) );
        }
        else if( sort.equals("favourite") ){
            listBaiDang.sort(Comparator.comparingInt(t -> t.getLike().size()));
        }

        if( filter.equals("noanswer") ){
            listBaiDang = listBaiDang.stream().filter( b -> b.getBinhLuans().isEmpty()).collect(Collectors.toList());
        }
        else if( filter.equals("mostanswer") ){
            listBaiDang = listBaiDang.stream().filter( b -> !b.getBinhLuans().isEmpty()).collect(Collectors.toList());
            listBaiDang.sort(Comparator.comparingInt(t -> t.getBinhLuans().size()));
        }
        if( !q.isEmpty() ){
            listBaiDang = listBaiDang
                    .stream()
                    .filter(b -> b.getNoidung().contains(q) || b.getTieude().contains(q))
                    .collect(Collectors.toList());
        }
        if( tagUsed != null ){
            if( tagUsed.length > 0){
                listBaiDang = listBaiDang.stream().filter( b -> b.getTag().stream().anyMatch( t -> Arrays.stream(tagUsed).anyMatch( tag ->tag.equals(t.getTenTag())) ) ).collect(Collectors.toList());
            }
        }
        int start = Math.min(offset * HomeController.numberPage, listBaiDang.size());
        int end = Math.min(offset * HomeController.numberPage + pageSize, listBaiDang.size());
        int number = listBaiDang.size();

        List<BaiDang> pageContent = listBaiDang.subList(start, end);
        return new pageQuestion_BaiDang(
                new PageImpl<>(pageContent, PageRequest.of(offset, pageSize), listBaiDang.size()),
                (int) Math.ceil((double) number / (double) HomeController.numberPage) == 0 ? 1 : (int) Math.ceil((double) number / (double) HomeController.numberPage)
        );
    }

    @Override
    public Integer tongBaiDang() {
        return baiDangRepo.getNumberPage();
    }

    @Override
    public void deleteQuestion(Long baidang) {
        BaiDang baiDang = new BaiDang();
        if( redisTemplate.opsForValue().get(HomeController.KEY_BD + ":" + baidang) != null){
            baiDang = (BaiDang) redisTemplate.opsForValue().get(HomeController.KEY_BD + ":" + baidang);
        }else {
            baiDang = baiDangRepo.findById(baidang).orElseThrow(() -> new EntityNotFoundException("BaiDang not found"));
        }
        baiDangRepo.delete(baiDang);
        System.out.println("=============================");
        System.out.println(HomeController.KEY + ":" + baiDang.getThongTin().getId());
        System.out.println("=============================");
        redisTemplate.opsForValue().set( HomeController.KEY + ":" + baiDang.getThongTin().getId(), thongTinRepo.findById(baiDang.getThongTin().getId()).get(), 4, TimeUnit.HOURS );
        redisTemplate.delete( HomeController.KEY_BD + ":" + baidang);
    }

    @Override
    public pageQuestion_BaiDang layBaiDangTheoDoi(Long thongtinId, Integer soluong, String q) {
        Optional<ThongTin> thongtin = thongTinRepo.findById(thongtinId);
        if( thongtin.isEmpty() ) throw new RuntimeException("Thong Tin Khong Hop Le");
        List<BaiDang> baiDangs = thongtin.get().getBaiDang_Luu();
        baiDangs = baiDangs.stream().filter( b -> b.getNoidung().contains(q) || b.getTieude().contains(q)).collect(Collectors.toList());
        // Kiểm tra nếu số lượng yêu cầu lớn hơn số lượng thực sự có, chỉ lấy số lượng thực sự có
        int soLuongThatSuCo = Math.min(soluong, baiDangs.size());
        // Tạo một trang mới chỉ chứa số lượng bình luận được yêu cầu
        pageQuestion_BaiDang p = new pageQuestion_BaiDang();
        p.setTotalPage(baiDangs.size());
        if (soLuongThatSuCo > 0) {
            Page<BaiDang> page = new PageImpl<>(baiDangs.subList(0, soLuongThatSuCo), PageRequest.of(0, soLuongThatSuCo), baiDangs.size());
            p.setBaiDangPage(page);
            return p;
        } else {
            return p;
        }
    }
}
