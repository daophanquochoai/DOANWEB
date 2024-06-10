package com.nhom29.Cotnroller;

import com.nhom29.Configuration.Security;
import com.nhom29.DTO.ConvertThongTinDangKi;
import com.nhom29.DTO.Infomation;
import com.nhom29.DTO.ThongTinDangKi;
import com.nhom29.Model.ERD.Tag;
import com.nhom29.Model.ERD.TaiKhoan;
import com.nhom29.Model.ERD.TaiKhoan_ThongTin;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Redis.Inter.TagInter_Redis;
import com.nhom29.Service.Inter.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/xuli")
@RequiredArgsConstructor
@Slf4j
public class XuLiController {
    private final ValueApp valueApp;
    private final MailInter mailInter;
    private final Security security;
    private final ThongTinInter thongTinInter;
    private final TaiKhoanInter taiKhoanInter;
    private final UyQuyenInter uyQuyenInter;
    private final CloudinaryInter cloudinaryInter;
    private final FollowInter followInter;
    private final BaiDangInter baiDangInter;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private final TagInter tagInter;
    private final TagInter_Redis tagInterRedis;

    // string random identify
    private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    // create regex check email
    private final String Email_Regex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final String KEY = "token:";
    private final Pattern pattern =Pattern.compile(Email_Regex);
    // save info identyfi and user need change password

    public String random( int len){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for( int i  = 0 ; i < len ; i++ ){
            int randomNumber = random.nextInt(CHARACTERS.length());
            char ramdomChar = CHARACTERS.charAt(randomNumber);
            sb.append(ramdomChar);
        }
        return sb.toString();
    }
//    đã nhập email vào và post lên kiểm tra
    @PostMapping("/email")
    public String guiMa(@RequestParam(value = "emailReceiver") String emailReceiver){
        ThongTin thongTin = new ThongTin();
        Optional<ThongTin> temp = thongTinInter.layThongTInByEmail(emailReceiver);
        if( temp.isEmpty()) return "redirect:/email?error=2";
        thongTin = temp.get();
        String identify = random(6);


        mailInter.sendMail(emailReceiver, identify);
        redisTemplate.opsForValue().set(KEY + thongTin.getId(), identify, 1, TimeUnit.MINUTES);
        return "redirect:/xuli/identify/" + thongTin.getId();
    }

    @PostMapping("/maxacnhan/{id}")
    public String xacNhanIdentify(@RequestParam("maxacnhan") List<String> maxacnhan,
                                  @PathVariable("id") Long id, Model model){
        String a = new String();
        for( String i : maxacnhan){
            a += i;
        }
        if( redisTemplate.opsForValue().get(KEY + id) != null && a.equals(redisTemplate.opsForValue().get(KEY + id))){
            redisTemplate.opsForValue().set(KEY + id, a, 10, TimeUnit.MINUTES);
            model.addAttribute("thongtin", thongTinInter.layThongTin(id));
            return "thayDoiMatKhau";
        }else{
            return "redirect:/email?error=1";
        }
    }

    @GetMapping("identify/{id}")
    public String xacNhan(@PathVariable String id, Model model){
        model.addAttribute("id", id);
        return "xacNhan";
    }

    @PostMapping("doimatkhau")
    @Transactional
    public String doiMatKhau(
            @RequestParam("xacnhan") String xacnhan,
            @RequestParam("id") Long id
    ){
        try{
            if( redisTemplate.opsForValue().get(KEY + id) == null) return "redirect:/email?error=3";
            ThongTin thongTin = new ThongTin();
            Optional<ThongTin> temp = thongTinInter.layThongTin(id);
            if( temp.isEmpty()) return "redirect:/email?error=3";
            thongTin = temp.get();
            thongTin.getTaiKhoanThongTin().getTaiKhoan().setPassword(passwordEncoder.encode(xacnhan));
            thongTinInter.updateMK(thongTin);
            return "redirect:/login";
        }catch (Exception ex){
            return "redirect:/email?error=3";
        }
    }

    @PostMapping("/user/{id}/doimatkhau")
    @Transactional
    public String userDoiMatKhau(
            @RequestParam("password") String password,
            @PathVariable( value = "id") Long id,
            @RequestParam(value = "purpose", defaultValue = "") String purpose
    ){
        try{
            ThongTin thongTin = new ThongTin();
            Optional<ThongTin> temp = thongTinInter.layThongTin(id);
            if( temp.isEmpty()) return "redirect:/error";
            thongTin = temp.get();
            thongTin.getTaiKhoanThongTin().getTaiKhoan().setPassword(passwordEncoder.encode(password));
            thongTinInter.updateMK(thongTin);
            return "redirect:" + purpose;
        }catch (Exception ex){
            return "redirect:/error";
        }
    }

    // xu li tao tai khoan
    @PostMapping("/taotaikhoan")
    @Transactional
    public String taoTaiKhoan(
            @Valid @ModelAttribute("thongtin") ThongTinDangKi thongTinDangKi,
            BindingResult bindingResult,
            Model model
            ) throws InterruptedException {

        if( bindingResult.hasErrors() ){
            Infomation infomation = new Infomation(valueApp.URLImage, valueApp.shortCutIcon);
            model.addAttribute("image", infomation);
            return "taoTaiKhoan";
        }

        if( thongTinDangKi.getEmail().isEmpty()) return "redirect:/taotaikhoan?error=email";
        if( thongTinDangKi.getTaikhoan().isEmpty()) return "redirect:/taotaikhoan?error=username";
        if( redisTemplate.opsForHash().get("thongtindangki", thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan()) == null){
            ConvertThongTinDangKi convert = new ConvertThongTinDangKi(thongTinDangKi.getHovaten(),
                    thongTinDangKi.getSodienthoai(),
                    thongTinDangKi.getEmail(),
                    thongTinDangKi.getTruonghoc(),
                    thongTinDangKi.getGioithieuveminh(),
                    thongTinDangKi.getTaikhoan(),
                    thongTinDangKi.getMatkhau());
            redisTemplate.opsForHash().put("thongtindangki", thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan(),convert);
        }

        ThongTin thongTin = new ThongTin();
        if( thongTinDangKi.getHovaten().isEmpty()) return "redirect:/taotaikhoan?error=name&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        Integer index = thongTinDangKi.getHovaten().lastIndexOf(" ");
        if( index > 0 ){
            thongTin.setTen(thongTinDangKi.getHovaten().substring(index+1));
            thongTin.setHo(thongTinDangKi.getHovaten().substring(0, index));
        }else{
            thongTin.setTen(thongTinDangKi.getHovaten().substring(index+1));
        }
        if( thongTinDangKi.getSodienthoai().length() == 10 && thongTinDangKi.getSodienthoai().charAt(0) == '0'){
            thongTin.setSdt(thongTinDangKi.getSodienthoai());
        }else{
            return "redirect:/taotaikhoan?error=sdt&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        }
        Matcher matcher = pattern.matcher(thongTinDangKi.getEmail());
        if( matcher.matches()){
            thongTin.setEmail(thongTinDangKi.getEmail());
        }else{
            return "redirect:/taotaikhoan?error=email&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        }

        if( thongTinInter.layThongTInByEmail(thongTinDangKi.getEmail()).isPresent() ){
            return "redirect:/taotaikhoan?error=email&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        }

        if( thongTinInter.layThongTinBySdt(thongTinDangKi.getSodienthoai()).isPresent()){
            return "redirect:/taotaikhoan?error=sdt&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        }
        if( thongTinDangKi.getTruonghoc()!=null){
            thongTin.setTruong(thongTinDangKi.getTruonghoc());
        }
        if( thongTinDangKi.getGioithieuveminh() != null){
            thongTin.setGioiThieu(thongTinDangKi.getGioithieuveminh());
        }
        TaiKhoan taiKhoan = new TaiKhoan();
        if(taiKhoanInter.taiKhoanTrung(thongTinDangKi.getTaikhoan())){
            taiKhoan.setUsername(thongTinDangKi.getTaikhoan());
        }else return "redirect:/taotaikhoan?error=username&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        if( thongTinDangKi.getMatkhau() != null){
            taiKhoan.setPassword(security.passwordEncoder().encode(thongTinDangKi.getMatkhau()));
        }else{
            return "redirect:/taotaikhoan?error=password&key=" + thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan();
        }
        thongTin.setProviderId("local");
        taiKhoan.setActive(true);
        if( uyQuyenInter.layQuyenUser("USER").isEmpty() ) return "redirect:/taotaikhoan?error=uyquyen";
        TaiKhoan_ThongTin taiKhoanThongTin = new TaiKhoan_ThongTin();
        taiKhoanThongTin.setUyQuyen(uyQuyenInter.layQuyenUser("USER").get());
        taiKhoanThongTin.setTaiKhoan(taiKhoan);
        thongTin.setTaiKhoanThongTin(taiKhoanThongTin);
        if( thongTinDangKi.getAvatar() == null){
            thongTin.setAnhDaiDien("https://kynguyenlamdep.com/wp-content/uploads/2022/06/avatar-con-ma-cute.jpg");
        }else{
            try{
                if( !thongTinDangKi.getAvatar().isEmpty()){
                    String url = cloudinaryInter.uploadFile(thongTinDangKi.getAvatar());
                    thongTin.setAnhDaiDien(url);
                }
            }catch (Exception ex){
                log.warn("{}", ex.getMessage());
            }
        }
        thongTinInter.luuThongTin(thongTin);
        redisTemplate.opsForHash().delete(thongTinDangKi.getEmail() + thongTinDangKi.getTaikhoan());
        return "redirect:/login";
    }
    @GetMapping("/follow/{baidangId}/{thongtinId}/{option}")
    public String follow(
            @PathVariable("baidangId") Long baidangId,
            @PathVariable("thongtinId") Long thongtinId,
            @PathVariable("option") Integer option,
            @RequestParam(value = "purpose", required = false) String purpose
            ){
        try{
            if( option == 1){
                followInter.follow(baidangId, thongtinId);
            }else{
                followInter.unfollow(baidangId, thongtinId);
            }
            if( purpose != null ){
                return "redirect:" + purpose;
            }else{
                return "redirect:/question/" + baidangId;
            }
        }catch (Exception ex){
            return "redirect:/error";
        }
    }
    @GetMapping("/like/{baidangId}/{thongtinId}/{option}")
    @Transactional
    public String like(
            @PathVariable("baidangId") Long baidangId,
            @PathVariable("thongtinId") Long thongtinId,
            @PathVariable("option") Integer option
    ){
        try{
            if( option == 1){
                followInter.like(baidangId, thongtinId);
            }else{
                followInter.unlike(baidangId, thongtinId);
            }
            return "redirect:/question/" + baidangId;
        }catch (Exception ex){
            return "redirect:/error";
        }
    }
    @PostMapping("/user/{userId}")
    @Transactional
    public String editUser(@PathVariable Long userId,
                           @Valid @ModelAttribute("infoXem") ThongTin thongTin,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @RequestParam(value = "avt", required = false) MultipartFile file
    ) throws IOException {
        if( bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đúng định dạng!!");
            return "redirect:/user/" + userId;
        }
        System.out.println("success");
        ThongTin thongTinGoc = new ThongTin();
        if( redisTemplate.opsForValue().get(HomeController.KEY + ":" + userId) != null){
            thongTinGoc = (ThongTin) redisTemplate.opsForValue().get(HomeController.KEY + ":" + userId);
        }else{
            ThongTin thongTinTemp = new ThongTin();
            Optional<ThongTin> temp = thongTinInter.layThongTin(userId);
            if( temp.isEmpty()) return "redirect:/error";
            thongTinTemp = temp.get();
            thongTinGoc = thongTinTemp;
        }
        ThongTin thongTin1 = thongTinInter.updateThongTin(thongTinGoc, file, thongTin);
        if(  thongTin1.getTaiKhoanThongTin() == null){
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +  thongTin1.getId(), thongTin1, 4, TimeUnit.HOURS );
        }else{
            redisTemplate.opsForValue().set( HomeController.KEY + ":" +   thongTin1.getTaiKhoanThongTin().getTaiKhoan().getUsername(), thongTin1, 4, TimeUnit.HOURS );
        }
        return "redirect:/user/" + userId;
    }

    // filter question page
    @PostMapping("/question/filter")
    @Transactional
    public String filterPageQuestion(
            @RequestParam(value = "filter") String filter,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "tags", required = false) String[] tags,
            @RequestParam(value = "q", required = false) String q
            ){
        String url = "";
        if( !filter.isEmpty() ){
            url += "filter=" + filter;
        }
        if( !sort.isEmpty() ){
            if( filter.isEmpty()){
                url += "sort=" + sort;
            }
            else{
                url += "&sort=" + sort;
            }
        }
        if( tags != null ){
            if( !filter.isEmpty() || !sort.isEmpty() ){
                url += "&";
            }
            for ( int i = 0 ; i < tags.length ; i++){
                if( i != tags.length -1){
                    url += "tagUsed=" + tags[i].replace(" ", "+") + "&";
                }else{
                    url += "tagUsed=" + tags[i].replace(" ", "+");
                }
            }
        }
        if( !q.isEmpty() ){
            if( tags != null ||  !filter.isEmpty() || !sort.isEmpty() ){
                url += "&";
            }
            url += "q=" + q;
        }
        if( !url.isEmpty()){
            return "redirect:/question?" + url;
        }else{
            return "redirect:/question";
        }
    }

    // delete question
    @PostMapping("/delete/question/{baidang}")
    @Transactional
    public String deleteQuestion( @PathVariable Long baidang,
                                  @RequestParam(value = "purpose", defaultValue = "") String purpose
    ){
        try{
            baiDangInter.deleteQuestion(baidang);
            return "redirect:" + purpose;
        }catch (Exception ex){
            return "error";
        }
    }
    @PostMapping("/tag/save")
    @Transactional
    public String creatTag(
            @ModelAttribute("tag") Tag tag
    ){
        try {
            System.out.println(tag.getTenTag());
            System.out.println(tag.getNoidung());

            List<Tag> tagList = tagInter.getAllTag();
            tagInter.saveTagALl(tag, tagList);
            if( redisTemplate.opsForValue().get("TAG") == null ){
                tagInterRedis.updateTag();
            }
            return "redirect:/tag";
        }catch (Exception ex)
        {
            return "redirect:/error";
        }
    }
}

