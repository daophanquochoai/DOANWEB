package com.nhom29.Cotnroller;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Data
@Controller
public class ValueApp {
    @Value("${imageUrl}")
    public String URLImage;

    @Value("${shortCutIcon}")
    public String shortCutIcon;
}
