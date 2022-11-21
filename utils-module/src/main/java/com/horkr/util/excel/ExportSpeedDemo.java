package com.horkr.util.excel;

import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.WorkbookUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.List;

/**
 * @author 卢亮宏
 */
public class ExportSpeedDemo {
    private static final Logger log = LogManager.getLogger(EasyExcelUtil.class);
    public static void easyExport(File file) throws Exception {
        String data = FileUtils.readFileToString(new File("D:\\test\\excelData.json"), "utf-8");
        List<List> allRows = JSON.parseArray(data, List.class);
        ExcelWriterSheetBuilder builder = EasyExcel.write(file)
                //设置Sheet名字
                .sheet("Sheet");
        builder.doWrite(allRows);


    }

    public static void poiExport(File file) throws Exception {
        String data = FileUtils.readFileToString(new File("D:\\test\\excelData.json"), "utf-8");
        List<List> allRows = JSON.parseArray(data, List.class);
        Workbook book = WorkbookUtil.createBook(true);
        Sheet one = WorkbookUtil.getOrCreateSheet(book, 0);
        cn.hutool.poi.excel.ExcelWriter writer = allRows.size() > 1000 ? new BigExcelWriter(one) : new cn.hutool.poi.excel.ExcelWriter(one);

        // 写入行数据
        for (List row : allRows) {
            writer.writeRow(row, false);
        }
    }

    public static void main(String[] args) throws Exception {
        log.info("start");
        File temp = File.createTempFile("temp", ".xlsx");
        easyExport(temp);
        log.info("end export");

        log.info("down");
    }
}
