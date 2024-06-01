package com.nhom29.Service.Oauth2.security;

import com.nhom29.Exception.BaseException;
import com.nhom29.Model.ERD.ThongTin;
import com.nhom29.Repository.ThongTinRepository;
import com.nhom29.Repository.TaiKhoan_ThongTInRepo;
import com.nhom29.Repository.UyQuyenRepository;
import com.nhom29.Service.Oauth2.OAuth2UserDetailFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nhom29.Service.Oauth2.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CustomOAuth2UserDetailService extends DefaultOAuth2UserService {
    @Autowired
    private ThongTinRepository thongTinRepository;
    @Autowired
    private UyQuyenRepository uyQuyenRepository;
    @Autowired
    private TaiKhoan_ThongTInRepo taiKhoanThongTInRepo;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try{
            return checkingOAuth2User(userRequest, oAuth2User);
        }catch ( AuthenticationException e){
            throw e;
        }catch (Exception ex){
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
    private OAuth2User checkingOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws BaseException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserDetailFactory.getOAuth2UserDetials(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );
        if(ObjectUtils.isEmpty(oAuth2UserRequest)){
            throw new BaseException("400", "Can not found oauth2 uer from properties");
        }
        Optional<ThongTin> thongTin = thongTinRepository.findByEmail(oAuth2UserInfo.getEmail());
        ThongTin us;
        if ( thongTin.isPresent() ){
            us = thongTin.get();
            if( !us.getProviderId().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())){
                throw new BaseException("400", "Invalid side login with " + us.getProviderId());
            }
            us = updateOAuth2UserDetail(us, oAuth2UserInfo);
        }else{
            us = registerNewOAuth2UserDetail(oAuth2UserRequest, oAuth2UserInfo);
        }
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("USER"));
        return new OAuth2UserDetailCustom(
                us.getId(),
                us.getEmail(),
                list
        );
    }

    public ThongTin updateOAuth2UserDetail(ThongTin user, OAuth2UserInfo oAuth2UserInfo){
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setAnhDaiDien(oAuth2UserInfo.getImageUrl());
        return thongTinRepository.save(user);
    }
    public ThongTin registerNewOAuth2UserDetail( OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserDetials){
        ThongTin user = new ThongTin();
        user.setTen(oAuth2UserDetials.getName());
        user.setEmail(oAuth2UserDetials.getEmail());
        user.setAnhDaiDien(oAuth2UserDetials.getImageUrl());
        user.setProviderId(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        return thongTinRepository.save(user);


    }
}
