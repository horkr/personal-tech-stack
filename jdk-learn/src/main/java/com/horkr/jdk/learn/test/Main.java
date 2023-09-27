package com.horkr.jdk.learn.test;

import okhttp3.*;

import java.io.IOException;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/xml");
        RequestBody body = RequestBody.create(mediaType, "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n\t<globalInfo>\r\n\t\t<version>1.0</version>\r\n\t\t<taxpayerId>91340000711771143J</taxpayerId>\r\n\t\t    <interfaceCode> INCOME_CHECK</interfaceCode>\r\n\t\t<authorizationCode></authorizationCode>\r\n\t</globalInfo>\r\n\t<returnStateInfo>\r\n\t\t<returnCode></returnCode>\r\n\t\t<returnMessage></returnMessage>\r\n\t</returnStateInfo>\r\n\t<data>\r\n\t\t<dataDescription>\r\n\t\t\t<zipCode>0</zipCode>\r\n\t\t\t<encryptCode>0</encryptCode>\r\n\t\t\t<codeType>0</codeType>\r\n\t\t</dataDescription>\r\n\t\t<content>PFJFUVVFU1RfQ09NTU9OX0xSIGNsYXNzPSJSRVFVRVNUX0NPTU1PTl9MUiI+IAogICAgPENPTU1PTl9MUl9IRUFERVJTPiAKICAgICAgICA8Q09NTU9OX0xSX0hFQURFUiBjbGFzcz0iQ09NTU9OX0xSX0hFQURFUiI+IAogICAgICAgICAgICA8RlBETT4wNDQwMDIxMDAxMTM8L0ZQRE0+IAogICAgICAgICAgICA8RlBITT4wMDAyMTI1NDwvRlBITT4KCSAgICAgICAgPEtQUlE+MjAyMTAzMjI8L0tQUlEgPgo8SkU+OTkwMC45OTwvSkU+CgkgICAgICAgIDxKWU0+PC9KWU0+CiAgCSAgIDwvQ09NTU9OX0xSX0hFQURFUj4KCTwvQ09NTU9OX0xSX0hFQURFUlM+CiA8L1JFUVVFU1RfQ09NTU9OX0xSPgo=</content>\r\n\t</data>\r\n</interface>\r\n");
        Request request = new Request.Builder()
                .url("https://tax.iflytek.com/services/incomeWebService?wsdl")
                .method("POST", body)
                .addHeader("Content-Type", "application/xml")
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
    }


    private static void tes() throws Exception{
        String authVal = "Basic " + Base64.getUrlEncoder().encodeToString(("TES-OKGFDZJJ" + ":" + "zeIZby9IWftfGkEdZsyWsBSd38AC7UiS").getBytes());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"requestname\": \"差旅报销-卢亮宏\",\r\n    \"creator\": \"10029460\",\r\n    \"receiver\": \"10029460\",\r\n    \"workflowname\": \"差旅报销\",\r\n    \"pcurl\": \"http://www.baidu.com\",\r\n    \"viewtype\": \"0\",\r\n    \"appurl\": \"http://www.baidu.com\",\r\n    \"syscode\": \"tes\",\r\n    \"nodename\": \"直属领导\",\r\n    \"receivets\": \"1693554308000\",\r\n    \"receivedatetime\": \"2023-09-01 15:44:00\",\r\n    \"isremark\": \"0\",\r\n    \"createdatetime\": \"2023-09-01 15:44:00\",\r\n    \"flowid\": \"tes001\"\r\n}");
        Request request = new Request.Builder()
                .url("https://ipaas-uat.sungrow.cn/ipaassit/TES/TES001_ReceiveRequestInfoByJson/1.0.1")
                .method("POST", body)
//                .addHeader("Content-Type", "application/json; charset=UTF-8")
//                .addHeader("Authorization", "Basic VEVTLU9LR0ZEWkpKOnplSVpieTlJV2Z0ZkdrRWRac3lXc0JTZDM4QUM3VWlT")
//                .addHeader("Authorization", authVal)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        System.out.println(string);
    }
}
