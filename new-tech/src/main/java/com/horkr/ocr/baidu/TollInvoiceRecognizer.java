package com.horkr.ocr.baidu;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

/**
 * 过路过桥费发票识别
 * @author 卢亮宏
 * @date 2023/09/14
 */
class TollInvoiceRecognizer extends AbstractOcrRequestStarter{
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public JSONObject call() throws IOException{
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/bus_ticket?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return JSONObject.parseObject(response.body().string());
    }


}