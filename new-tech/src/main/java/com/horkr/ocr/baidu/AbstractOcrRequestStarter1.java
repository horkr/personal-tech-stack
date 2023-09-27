package com.horkr.ocr.baidu;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 卢亮宏
 * @date 2023/09/14
 */
public abstract class AbstractOcrRequestStarter1 {
    private static final Logger log = LogManager.getLogger(AbstractOcrRequestStarter1.class);
    /**
     * http 客户端
     */
    protected static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
    public static final String MEDIA_TYPE_JSON = "application/json";
    public static final String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?client_id=%s&client_secret=%s&grant_type=client_credentials";
    public static final String POST = "POST";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";
    public static final String CLIENT_ID = "KKaQQmb6S3yuhDsG1MLA5bFC";
    public static final String CLIENT_SECRET = "z7Ut2wAfhCWsyecQctPYFVHhjPbnMbij";
    public static final String ACCESS_TOKEN = "access_token";
    public static String CURRENT_TOKEN = "";


    /**
     * 获取Token
     * @return {@link String}
     */
    private String getToken(){
        String tokenUrl = String.format(TOKEN_URL, CLIENT_ID, CLIENT_SECRET);
        JSONObject response = post(MEDIA_TYPE_JSON, tokenUrl, new JSONObject());
        return response.getString(ACCESS_TOKEN);
    }


    /**
     * 发起post请求
     * @param mediaTypeText mediaType文本
     * @param url           请求的url
     * @param bodyParams    post参数
     * @return {@link JSONObject}
     */
    public JSONObject post(String mediaTypeText, String url, JSONObject bodyParams){
        MediaType mediaType = MediaType.parse(mediaTypeText);
        RequestBody body = RequestBody.create(mediaType, bodyParams.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .method(POST, body)
                .addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)
                .addHeader(ACCEPT, MEDIA_TYPE_JSON)
                .build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            return JSONObject.parseObject(response.body().string());
        }catch (Exception e){
            log.error("发起post请求出错",e);
            throw new IllegalStateException(e);
        }
    }
}
