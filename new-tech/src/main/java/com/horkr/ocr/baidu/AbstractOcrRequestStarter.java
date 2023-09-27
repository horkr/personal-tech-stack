package com.horkr.ocr.baidu;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.*;

public abstract class AbstractOcrRequestStarter{
    public static final String API_KEY = "KKaQQmb6S3yuhDsG1MLA5bFC";
    public static final String SECRET_KEY = "z7Ut2wAfhCWsyecQctPYFVHhjPbnMbij";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();


    abstract JSONObject call() throws IOException;
    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return JSONObject.parseObject(response.body().string()).getString("access_token");
    }

}