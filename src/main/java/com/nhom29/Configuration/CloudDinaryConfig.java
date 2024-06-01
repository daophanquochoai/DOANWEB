package com.nhom29.Configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.HashMap;

@Configuration
public class CloudDinaryConfig {
    private final String CLOUD_NAME = "dkbukqhmr";
    private final String API_KEY = "182839138267935";
    private final String API_SECRET = "MDpABd4odJ9f9XSrbY5kbkxPCWw";

    @Bean
    public Cloudinary cloudPlatform(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }
}
