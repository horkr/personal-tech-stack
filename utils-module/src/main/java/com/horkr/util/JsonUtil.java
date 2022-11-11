package com.horkr.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonUtil {
    public static JSONObject transferToJson(Object o) {
        return JSONObject.parseObject(JSONObject.toJSONString(o));
    }

    private static Logger logger = LogManager.getLogger(JsonUtil.class);
    public static void main(String[] args) {
        logger.error("sss");
    }
}
