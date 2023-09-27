package com.horkr.spring.learn.http.cookie;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
public class HttpController {


    @GetMapping("/cookieTest")
    public Object cookie(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie=new Cookie("name","Tom");
        //设置Maximum Age
        cookie.setMaxAge(1000);
        //设置cookie路径为当前项目路径
        cookie.setPath(request.getContextPath());
        //添加cookie
        response.addCookie(cookie);
        return "zzz";
    }


    @GetMapping("/cookieGet")
    public Object cookieGet(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies).map(cookie->JSONObject.parseObject(JSONObject.toJSONString(cookie))).collect(Collectors.toList());
    }

    @GetMapping("/startRedirect")
    public void startRedirect(String key,String vaule,HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format("http://localhost:10002/redirect?key=%s&value=%s",key,vaule));
    }


    @GetMapping("/redirect")
    public Object redirect(String key,String value){
        return key+":"+value;
    }
}
