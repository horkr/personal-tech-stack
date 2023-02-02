package com.horkr.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * @author 卢亮宏
 */
public class FastJsonTest {
    public static class DD{
        private String name;

        private Date date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject().fluentPut("name", "b").fluentPut("date","20220309T183222");
        DD dd = jsonObject.toJavaObject(DD.class);
        System.out.println();
    }
}
