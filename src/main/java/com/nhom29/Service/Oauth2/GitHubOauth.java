package com.nhom29.Service.Oauth2;
import java.sql.Blob;
import java.util.Map;

public class GitHubOauth extends OAuth2UserInfo {
    public GitHubOauth( Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("login");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
