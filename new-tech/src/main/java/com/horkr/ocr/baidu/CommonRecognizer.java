package com.horkr.ocr.baidu;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.*;

class CommonRecognizer extends AbstractOcrRequestStarter{
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public JSONObject call() throws IOException{
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/multiple_invoice?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return JSONObject.parseObject(response.body().string());
    }

    public static void main(String[] args) {

    }

}