package com.nhom29.Service.Oauth2;

import java.util.Map;
public class FaceBookOAuth extends OAuth2UserInfo {

    public FaceBookOAuth(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email") + "facebook";
    }

    @Override
    public String getImageUrl() {
        if(attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return "https://kynguyenlamdep.com/wp-content/uploads/2022/06/avatar-con-ma-cute.jpg";
    }
}
