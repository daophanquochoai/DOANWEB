package com.nhom29.Service.Oauth2;
import com.nhom29.Exception.BaseException;
import com.nhom29.Model.OAuth2.Provider;

import java.util.Map;

public class OAuth2UserDetailFactory {
    public static OAuth2UserInfo getOAuth2UserDetials(String regisId, Map<String,Object> attributes) throws BaseException {
        if( regisId.equals(Provider.facebook.name())){
            return new FaceBookOAuth(attributes);
        }
        else if( regisId.equals(Provider.github.name())){
            return new GitHubOauth(attributes);
        }
        else {
            throw new BaseException("400", "Sorry! Login with " + regisId + " is not supported");
        }

    }
}
