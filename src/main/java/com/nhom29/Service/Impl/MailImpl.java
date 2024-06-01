package com.nhom29.Service.Impl;

import com.nhom29.Service.Inter.MailInter;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailImpl implements MailInter {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailSender;
    private LocalDateTime time = LocalDateTime.now();

    @Override
    public Boolean sendMail(String emailReceiver, String identify) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailSender);
            helper.setTo(emailReceiver);
            helper.setSubject("Khôi phục mật khẩu");
            helper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title>Change Account</title>\n" +
                            "    <link\n" +
                            "    rel=\"stylesheet\"\n" +
                            "    href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css\"\n" +
                            "    integrity=\"sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A==\"\n" +
                            "    crossorigin=\"anonymous\"\n" +
                            "    referrerpolicy=\"no-referrer\"\n" +
                            "    />\n" +
                            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\">\n" +
                            "</head>\n" +
                            "<body class=\"d-flex justify-content-center align-items-center w-100\">\n" +
                            "\n" +
                            "<section class=\"container-fluid d-flex justify-content-center align-items-center w-100\" style=\"height:200px\">\n" +
                            "        <div>\n" +
                            "            <div style=\"font-size: 24px;\" class=\"mb-4 mt-4\">Trang Web chia sẻ tâm tư, tình cảm, nổi buồn, lo âu</div>\n" +
                            "            <div class=\"w-100\"><img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/0/02/Stack_Overflow_logo.svg/1280px-Stack_Overflow_logo.svg.png\" style=\"width: 100%;height:80px;\"></div>\n" +
                            "            <div style=\"background-color: gray; color: white; text-align: center; font-size: 30px;\" class=\"mt-3 mb-3\">" +identify + "</div>\n" +
                            "        </div>\n" +
                            "</section>\n" +
                            "\n" +
                            "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM\" crossorigin=\"anonymous\"></script>\n" +
                            "</body>\n" +
                            "</html>"
                    , true);

            javaMailSender.send(message);
            time = LocalDateTime.now();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

//    @Override
//    public Long accept(String maxacnhan, Long id) {
//        if( matKhauTokenRepo.xacnhan(maxacnhan, id, LocalDateTime.now().plusMinutes(1)).isEmpty()) return 0L;
//        MatKhauToken matKhauToken = matKhauTokenRepo.xacnhan(maxacnhan, id, LocalDateTime.now().plusMinutes(1)).get();
//        return matKhauToken.getId();
//    }
//
//    @Override
//    public Boolean check(Long id) {
//        if( matKhauTokenRepo.kiemTraThayDoi(id).isEmpty() ) return false;
//        return true;
//    }
}
