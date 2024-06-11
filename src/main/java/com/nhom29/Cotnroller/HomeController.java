package com.nhom29.Cotnroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom29.DTO.*;
import com.nhom29.Model.ERD.*;
import com.nhom29.Redis.Inter.ReportInter;
import com.nhom29.Redis.Inter.TagInter_Redis;
import com.nhom29.Repository.ThongBao_ThongTinRepository;
import com.nhom29.Service.Inter.*;
import com.nhom29.Service.Oauth2.CurrentUser;
import com.nhom29.Service.Oauth2.security.OAuth2UserDetailCustom;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    public static Integer numberPage = 10;
    private final ThongTinInter thongTinInter;
    private final BaiDangInter baiDangInter;
    private final TagInter tagInter;
    private final ValueApp valueApp;
    private final BinhLuanInter binhLuanInter;
    private final CloudinaryInter cloudinaryInter;
    private final ThongBaoInter thongBaoInter;
    private final RedisTemplate redisTemplate;
    private final ReportInter reportInter;
    private final ThongBao_ThongTinRepository thongBaoThongTinRepository;


    private final TagInter_Redis tagInterRedis;

    public static final String KEY = "USER:";
    public static final String KEY_BD = "BAIDANG:";
    @GetMapping("/test")
    public String test(Model model){
        model.addAttribute("baidang", tagInterRedis.getAllTag());
        return "test";
    }
    // view dang nhap
    @GetMapping("/login")
    public String hello(Model model,
                        @RequestParam(value = "status", defaultValue = "") String status
                        ){
        Infomation infomation = new Infomation(valueApp.URLImage, valueApp.shortCutIcon);
        model.addAttribute("image", infomation);
        return "login";
    }
    @GetMapping("/prelogin")
    public String preLogin(@CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
                           Authentication authentication
    ){

        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                System.out.println("============================");
                System.out.println("DB");
                System.out.println("============================");
                Optional<ThongTin> thongTin = thongTinInter.layThongTinByUserName(userDetails.getUsername());
                if ( thongTin.isEmpty()) return "redirect:/logout";
                redisTemplate.opsForValue().set( KEY + ":" + userDetails.getUsername(),thongTin.get(), 4, TimeUnit.HOURS );
            }
            else{
                System.out.println("============================");
                System.out.println("REDIS");
                System.out.println("============================");
                redisTemplate.expire(KEY + ":" + userDetails.getUsername(), 4, TimeUnit.HOURS);
            }
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                System.out.println("============================");
                System.out.println("DB");
                System.out.println("============================");
                Optional<ThongTin> thongTin = thongTinInter.layThongTin(oAuth2UserDetailCustom.getId());
                if ( thongTin.isEmpty()) return "redirect:/logout";
                redisTemplate.opsForValue().set( KEY + ":" + oAuth2UserDetailCustom.getId(),thongTin.get(), 4, TimeUnit.HOURS );
            }
            else{
                System.out.println("============================");
                System.out.println("REDIS");
                System.out.println("============================");
                redisTemplate.expire(KEY + ":" + oAuth2UserDetailCustom.getId(), 4, TimeUnit.HOURS);
            }
        }
        if( redisTemplate.opsForValue().get("TAG") == null ){
            tagInterRedis.updateTag();
        }
        if( redisTemplate.opsForValue().get("REPORT") == null ){
            reportInter.updateReport();
        }
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(Model model, @CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       @RequestParam(value = "sort", defaultValue = "thoigiantao") String sort,
                       Authentication authentication,
                       HttpServletRequest request
    ) throws JsonProcessingException {
        if( redisTemplate.opsForValue().get("REPORT") == null ){
            reportInter.updateReport();
        }
        Report report = (Report) redisTemplate.opsForValue().get("REPORT");
        ThongTin thongTin = new ThongTin();
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);
        //infomation about BaiDang was paginationed and sort feilded
        model.addAttribute("page", baiDangInter.timBaiDangPhanTrang((page-1), numberPage, "like"));
        // All properti tag
        model.addAttribute("tags", tagInterRedis.getAllTag());
        // DTO fropm BaiDang
        model.addAttribute("baidang", new BaiDangDTO());
        Pagination pagination = new Pagination(baiDangInter.getNumberPage(),page);
        // infomation about pagination
        model.addAttribute("pagination", pagination);
        // value from application.p
        Infomation infomation = new Infomation(valueApp.URLImage, valueApp.shortCutIcon);
        model.addAttribute("image", infomation);

        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        model.addAttribute("request", request);
        model.addAttribute("report", report);
        return "home";
    }
    // view bi chan
    @GetMapping("error")
    public String denied(){
        return "error";
    }
    // view bi chan
    @PostMapping("/upload")
//    @Transactional
    public String uploadFile(@ModelAttribute("baidang") BaiDangDTO baiDangDTO,
                             @RequestParam(value = "fileInput", required = false) MultipartFile[] files,
                             @RequestParam(value = "tags", required = false) List<String> tags,
                             @RequestParam(value = "purpose") String purpose
    ) {
        try {
            BaiDang b = new BaiDang();
            ThongTin temp = new ThongTin();
            if( redisTemplate.opsForValue().get(KEY + ":" + baiDangDTO.getId()) != null){
                System.out.println("============================");
                System.out.println("REDIS");
                System.out.println("============================");
                temp = (ThongTin) redisTemplate.opsForValue().get(KEY + ":" + baiDangDTO.getId());
            }else{
                System.out.println("============================");
                System.out.println("DB");
                System.out.println("============================");
                Optional<ThongTin> t =  thongTinInter.layThongTin(baiDangDTO.getId());
                if( t.isEmpty()) return "error";
                temp = t.get();
            }
            b.setThongTin(temp);
            b.setThoigiantao(LocalDateTime.now());
            b.setTieude(baiDangDTO.getTieuDe());
            b.setNoidung(baiDangDTO.getNoiDung());

            // Xử lý tải tập tin và thêm hình ảnh vào bài đăng
            if (files != null) {
                Arrays.asList(files).forEach(file -> {
                    try {
                        if (!file.isEmpty()) {
                            String url = cloudinaryInter.uploadFile(file);
                            HinhAnh h = new HinhAnh();
                            h.setUrl(url);
                            b.getHinhAnh().add(h);
                        }
                    } catch (Exception ex) {
                        log.warn("{}", ex.getMessage());
                    }
                });
            }
            // Thêm các Tag vào bài đăng
            if (tags != null) {
                boolean checkUpdateRedis = false;
                for (String tagName : tags) {
                    Tag tag = tagInter.getTagByName(tagName);
                    if (tag != null) {
                        b.getTag().add(tag);
                    } else {
                        checkUpdateRedis = true;
                        // Nếu tag chưa tồn tại, bạn có thể tạo mới và thêm vào danh sách
                        Tag newTag = new Tag();
                        newTag.setTenTag(tagName);
                        newTag.setThoigiantao(LocalDateTime.now());
                        b.getTag().add(tagInter.save(newTag));
                    }
                }
                if( checkUpdateRedis ){
                    tagInterRedis.updateTag();
                }
            }

            // Lưu bài đăng vào cơ sở dữ liệu
             baiDangInter.saveBaiDang(b);
            if( temp.getTaiKhoanThongTin() == null){
                redisTemplate.opsForValue().set( KEY + ":" +  baiDangDTO.getId(),thongTinInter.layThongTin(baiDangDTO.getId()).get(), 4, TimeUnit.HOURS );
            }else{
                redisTemplate.opsForValue().set( KEY + ":" +  temp.getTaiKhoanThongTin().getTaiKhoan().getUsername(),thongTinInter.layThongTin(baiDangDTO.getId()).get(), 4, TimeUnit.HOURS );
            }
        } catch (Exception ex) {
            log.warn("{}", ex.getMessage());
            return "error"; // Trả về trang lỗi nếu có lỗi xảy ra
        }
        return "redirect:" + purpose;
    }
    // redict page question
    @GetMapping("/question")
    public String pageQuestion(
            Model model,
            @CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "filter", defaultValue = "default") String filter,
            @RequestParam(value = "sort", defaultValue = "default") String sort,
            @RequestParam(value = "tagUsed", defaultValue = "") String[] tagUsed,
            HttpServletRequest request,
            @RequestParam(value = "q", required = false, defaultValue = "") String q
    ) throws JsonProcessingException {
        //================================thong tin trang chu================================
        ThongTin thongTin = new ThongTin();
        if( redisTemplate.opsForValue().get("REPORT") == null ){
            reportInter.updateReport();
        }
        Report report = (Report) redisTemplate.opsForValue().get("REPORT");
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);
        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));
        // information from application file
        Infomation infomation = new Infomation(valueApp.getURLImage(), valueApp.getShortCutIcon());
        model.addAttribute("image", infomation);

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        model.addAttribute("request", request);
        model.addAttribute("q", q);
        //===========================================================================
        //infomation about BaiDang was paginationed and sort feilded
        pageQuestion_BaiDang listBaiDang = baiDangInter.timBaiDangPhanTrangVaLoc(page-1, numberPage, sort, filter, tagUsed,q );
        if( !q.isEmpty() ){
            List<BaiDang> filteredBaiDangList = listBaiDang.getBaiDangPage()
                    .stream()
                    .filter(b -> b.getNoidung().contains(q) || b.getTieude().contains(q))
                    .collect(Collectors.toList());

            // Tạo một đối tượng Page mới từ danh sách đã lọc
            listBaiDang.setBaiDangPage(new PageImpl<>(filteredBaiDangList));
        }
        model.addAttribute("page", listBaiDang.getBaiDangPage());
        // All properti tag
        List<Tag> listTag = tagInterRedis.getAllTag();
        if( tagUsed.length != 0){
            listTag = listTag.stream().filter( t-> Arrays.stream(tagUsed).noneMatch( tu -> tu.equals(t.getTenTag()) )).collect(Collectors.toList());
        }
        model.addAttribute("tags", listTag);
        // DTO fropm BaiDang
        model.addAttribute("baidang", new BaiDangDTO());
        Pagination pagination = new Pagination(listBaiDang.getTotalPage(), page);
        // infomation about pagination
        model.addAttribute("pagination", pagination);
        // All tag used
        model.addAttribute("tagUsed", tagUsed);
        // filter
        model.addAttribute("filter", filter.trim());
        // sort
        model.addAttribute("sort", sort.trim());
        // total baidang
        model.addAttribute("totalQuestion", baiDangInter.tongBaiDang());
        model.addAttribute("report", report);
        return "question";
    }

    // redict page detail question
    @GetMapping("/question/{baiDangId}")
    public String pageDetaiQuestion(Model model,@CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
                                    @PathVariable Long baiDangId,
                                    Authentication authentication,
                                    @RequestParam(value = "page", defaultValue = "3") Integer soluong,
                                    @RequestParam(value = "sort", defaultValue = "macdinh") String sort,
                                    HttpServletRequest request
                                    ) throws JsonProcessingException {
        ThongTin thongTin = new ThongTin();
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);
        // doc thong bao
        thongBaoInter.docThongBaoTheoNguoi(thongTin.getId(), baiDangId);
        // deltai baidang
        BaiDang baiDang = new BaiDang();
        if( redisTemplate.opsForValue().get(KEY_BD + ":" + baiDangId) != null){
            System.out.println("=============view===============");
            System.out.println("REDIS");
            System.out.println("============================");
            baiDang = (BaiDang) redisTemplate.opsForValue().get(KEY_BD + ":" + baiDangId);
            System.out.println("============================");
            System.out.println(baiDang.getBinhLuans().size());
            System.out.println("============================");
        }else{
            System.out.println("============================");
            System.out.println("DB");
            System.out.println("============================");
            Optional<BaiDang> baiDang_pre = baiDangInter.layChiTietBaiDang(baiDangId);
            if( baiDang_pre.isEmpty()) return "redirect:/home/error=baidangId";
            redisTemplate.opsForValue().set(KEY_BD + ":" + baiDang_pre.get().getId(), baiDang_pre.get(), 4, TimeUnit.HOURS);
            baiDang = baiDang_pre.get();
        }
        // user followed baidang
        ThongTin finalThongTin = thongTin;
        Boolean fl = baiDang.getLuu().stream().anyMatch(t -> t.getId().equals(finalThongTin.getId()));
        model.addAttribute("follow", fl);

        // user liked baidang
        Boolean like = baiDang.getLike().stream().anyMatch(t -> t.getId().equals(finalThongTin.getId()));
        model.addAttribute("like", like);

        // information about baidang
        model.addAttribute("chiTietBaiDang", baiDang);
        // upload new question
        model.addAttribute("baidang", new BaiDangDTO());
        // All properti tag
        model.addAttribute("tags", tagInterRedis.getAllTag());
        // information from application file
        Infomation infomation = new Infomation(valueApp.getURLImage(), valueApp.getShortCutIcon());
        model.addAttribute("image", infomation);
        Page<BinhLuan> binhLuanPage = binhLuanInter.layBinhLuanTheoBaiDangVaPhanTrang(baiDangId,soluong,sort);
        // pagination binhluan
        model.addAttribute("dachsachbinhluan",binhLuanPage);
        // infomation about page and size binhluan
        Pagination pagination = new Pagination((int) binhLuanPage.getTotalElements(), soluong);
        model.addAttribute("pagination", pagination);
        // format binhluan
        BinhLuan binhLuan = new BinhLuan();
        model.addAttribute("format", binhLuan);
        // sort
        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        model.addAttribute("request", request);
        model.addAttribute("sort", sort);
        return "chiTietCauHoi";
    }

    @PostMapping("/upload/comment")
    @Transactional
    public String uploadComment(@ModelAttribute BinhLuan binhLuan,
                                @RequestParam(value = "fileInput", required = false) MultipartFile[] files,
                                @RequestParam(value = "nguoibinhluanId") Long nguoibinhluanId,
                                @RequestParam(value = "baidangId") Long baidangId,
                                @RequestParam(value = "page", defaultValue = "3") Integer soluong,
                                @RequestParam(value = "sort", defaultValue = "macdinh") String sort) throws InterruptedException {
        BinhLuan bl = new BinhLuan();
        if( binhLuan.getNoidung() != null ){
            bl.setNoidung(binhLuan.getNoidung());
        }
        bl.setDate(LocalDateTime.now());
        if (files != null) {
            Arrays.asList(files).forEach(file -> {
                try {
                    if (!file.isEmpty()) {
                        String url = cloudinaryInter.uploadFile(file);
                        HinhAnhBinhLuan h = new HinhAnhBinhLuan();
                        h.setUrl(url);
                        bl.getHinhAnh().add(h);
                    }
                } catch (Exception ex) {
                    log.warn("{}", ex.getMessage());
                }
            });
        }
        Optional<ThongTin> thongTin = thongTinInter.layThongTin(nguoibinhluanId);
        if( thongTin.isEmpty() ) return "redirect:/question/" + baidangId + "?error=nguoibinhluan";
        bl.setThongTin(thongTin.get());
        Optional<BaiDang> baiDang = baiDangInter.layChiTietBaiDang(baidangId);
        if( baiDang.isEmpty()) return "redirect:/question/" + baidangId + "?error=baidang";
        bl.setBaidang(baiDang.get());
        BinhLuan b = binhLuanInter.luuBinhLuan(bl);
        redisTemplate.opsForValue().set(KEY_BD + ":" + baidangId,  baiDangInter.layChiTietBaiDang(baidangId).get(), 4, TimeUnit.HOURS);
        BaiDang bd = (BaiDang) redisTemplate.opsForValue().get(KEY_BD + ":" + baidangId);
        System.out.println("=============upload===============");
        System.out.println(bd.getBinhLuans().toArray().length);
        System.out.println("============================");
        if(  thongTin.get().getTaiKhoanThongTin() == null){
            redisTemplate.opsForValue().set( KEY + ":" +   thongTin.get().getId(), thongTin.get(), 4, TimeUnit.HOURS );
        }else{
            redisTemplate.opsForValue().set( KEY + ":" +   thongTin.get().getTaiKhoanThongTin().getTaiKhoan().getUsername(), thongTin.get(), 4, TimeUnit.HOURS );
        }
        return "redirect:/question/" + baidangId + "?page=" + soluong + "&sort=" + sort;
    }
    @GetMapping("/email")
    public String pageEmail(@RequestParam(value = "error", defaultValue = "") String error, Model model){
            model.addAttribute("error", error);
            return "layLaiMatKhau";
    }

    @GetMapping("/taotaikhoan")
    public String pageTaoTaiKhoan(@RequestParam(value = "error", defaultValue = "") String error,
                                  Model model,
                                  @RequestParam(value = "", defaultValue = "") String key
    ){
        ConvertThongTinDangKi c = (ConvertThongTinDangKi) redisTemplate.opsForValue().get("thongtindangki::" + key);
        ThongTinDangKi thongtin = new ThongTinDangKi();
        if( c != null ){
            thongtin.setHovaten(c.getHovaten());
            thongtin.setEmail(c.getEmail());
            thongtin.setMatkhau(c.getMatkhau());
            thongtin.setSodienthoai(c.getSodienthoai());
            thongtin.setTaikhoan(c.getTaikhoan());
            thongtin.setTruonghoc(c.getTruonghoc());
            thongtin.setGioithieuveminh(c.getGioithieuveminh());
        }
        model.addAttribute("thongtin", thongtin);
        Infomation infomation = new Infomation(valueApp.URLImage, valueApp.shortCutIcon);
        model.addAttribute("image", infomation);
        return "taoTaiKhoan";
    }

    @GetMapping("/tag")
    public String pageTag(Model model,@CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
                          Authentication authentication,
                          @RequestParam(value = "sort", defaultValue = "macdinh") String sort,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "q", defaultValue = "") String q,
                          HttpServletRequest request

    ) throws JsonProcessingException {
        //================================thong tin trang chu================================
        // info of account login
        ThongTin thongTin = new ThongTin();
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);
        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));
        // information from application file
        Infomation infomation = new Infomation(valueApp.getURLImage(), valueApp.getShortCutIcon());
        model.addAttribute("image", infomation);

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        //================= thong tin về tag =================================
//        model.addAttribute("tagList", tagInterRedis.getAllTag());
        Page<Tag> pageTag = tagInter.phanTrangTag(page - 1, 8 , sort, q);
        model.addAttribute("tagList", pageTag);
        model.addAttribute("tag", new Tag());
        Integer tongTrang = pageTag.getTotalPages();
        Pagination pagination = new Pagination(tongTrang, page);
        System.out.println(tongTrang);
        System.out.println(page);
        model.addAttribute("pagination", pagination);
        model.addAttribute("request", request);
        model.addAttribute("q", q);
        return "tag";
    }

    //===================USER_DETAIL===============
    @GetMapping("/user/{id}")
    public String pageUserDetail( Model model,
                                  @PathVariable Long id,
                                  @CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
                                  Authentication authentication,
                                  HttpServletRequest request
    ) throws JsonProcessingException {
        //================================thong tin trang chu================================
        // info of account login
        ThongTin thongTin = new ThongTin();
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);

        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));
        // information from application file
        Infomation infomation = new Infomation(valueApp.getURLImage(), valueApp.getShortCutIcon());
        model.addAttribute("image", infomation);

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        model.addAttribute("request", request);
        //=======================================thong tin nguoi can xem===============================================
        ThongTin thongTinXem = new ThongTin();
        if( thongTin.getId() != id){
            Optional<ThongTin> temp = thongTinInter.layThongTin(id);
            if( temp.isEmpty()) return "redirect:/logout";
            thongTinXem = temp.get();
        }else{
            thongTinXem = thongTin;
        }
        //infomation
        model.addAttribute("infoXem", thongTinXem);
        // info bai dang
        UserDetail userDetail = new UserDetail(thongTinXem.getBinhLuans().size(), thongTinXem.getBaiDangs().size());
        model.addAttribute("colcu", userDetail);
        return "user-detail";
    }

    // page saved
    @GetMapping("/saved")
    public String pageSaved(
            @CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
            Authentication authentication,
            Model model,
            @RequestParam(value = "q", defaultValue = "", required = false) String q,
            @RequestParam(value = "page", defaultValue = "5", required = false) Integer soluong,
            HttpServletRequest request
    ) throws JsonProcessingException {
        //============================default================================
        ThongTin thongTin = new ThongTin();
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);
        //infomation about BaiDang was paginationed and sort feilded
        pageQuestion_BaiDang page =  baiDangInter.layBaiDangTheoDoi(thongTin.getId(), soluong, q);
        model.addAttribute("page", page.getBaiDangPage());

        Pagination pagination = new Pagination(page.getTotalPage(),soluong);
        // infomation about pagination
        model.addAttribute("pagination", pagination);
        // value from application.p
        Infomation infomation = new Infomation(valueApp.URLImage, valueApp.shortCutIcon);
        model.addAttribute("image", infomation);

        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        model.addAttribute("request", request);
        //===================================================================
        model.addAttribute("q", q);
        return "luu";
    }

    //page user
    @GetMapping("/users")
    public String pageUser( Model model, @CurrentUser OAuth2UserDetailCustom oAuth2UserDetailCustom,
                            @RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "q", required = false,defaultValue = "") String q,
                            Authentication authentication,
                            HttpServletRequest request ) throws JsonProcessingException {
        //=============================default===================================
        ThongTin thongTin = new ThongTin();
        if( oAuth2UserDetailCustom == null ){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if( redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + userDetails.getUsername());
        }else{
            if( redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId()) == null){
                return "redirect:/logout";
            }
            thongTin = (ThongTin) redisTemplate.opsForValue().get( KEY + ":" + oAuth2UserDetailCustom.getId());
        }
        // infomation about user logined
        model.addAttribute("info", thongTin);
//        //infomation about BaiDang was paginationed and sort feilded
//        model.addAttribute("page", baiDangInter.timBaiDangPhanTrang((page-1), numberPage, "like"));
        // All properti tag
//        model.addAttribute("tags", tagInterRedis.getAllTag());
        // DTO fropm BaiDang
//        model.addAttribute("baidang", new BaiDangDTO());
//        Pagination pagination = new Pagination(baiDangInter.getNumberPage(),page);
        // infomation about pagination
//        model.addAttribute("pagination", pagination);
        // value from application.p
        Infomation infomation = new Infomation(valueApp.URLImage, valueApp.shortCutIcon);
        model.addAttribute("image", infomation);

        // all notice
        model.addAttribute("notice", thongBaoInter.layThongBaoTheoNguoi(thongTin.getId()));
        model.addAttribute("noticeNotSeen", thongBaoInter.thongBaoChuaXem(thongTin.getId()));

        List<Object> baiDangIdsList = thongTin.getBaiDang_Luu()
                .stream()
                .map(b -> b.getId())
                .collect(Collectors.toList());;

        // Convert the list to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(baiDangIdsList);
        // Pass the jsonString to your Thymeleaf template
        model.addAttribute("baiDangJson", jsonString);
        model.addAttribute("request", request);
        //===============================================================================================
        pageUsers_ThongTin pageUsersThongTin = thongTinInter.layThongTinTheoPageVaQ(page-1, q);
        model.addAttribute("users", pageUsersThongTin.getThongTins());
        //pagination
        model.addAttribute("pagination", pageUsersThongTin.getPagination());
        //search
        model.addAttribute("q", q);
        return "user";
    }
}

