package com.horkr.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.horkr.util.dto.DemoData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 **/

public class EasyExcelUtil {
    private static final Logger log = LogManager.getLogger(EasyExcelUtil.class);

    private static void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = "D:\\test\\test.xlsx";
        WriteSheet writeSheet = EasyExcel.writerSheet(1).build();
        write(fileName,writeSheet,data(),DemoData.class);
    }

    public static void main(String[] args) {
        simpleWrite();
    }

    public static <T> void write(String fileName, int sheetNo, String sheetName, Collection<T> data, Class<T> tClass) {
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, sheetName).build();
        write(fileName, writeSheet, data, tClass);
    }

    public static <T> void write(String fileName, WriteSheet writeSheet, Collection<T> data, Class<T> tClass) {
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, tClass).build()) {
            excelWriter.write(data, writeSheet);
        }
    }


    private static List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 100; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}
