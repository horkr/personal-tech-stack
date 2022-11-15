package com.horkr.util.excel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static cn.hutool.poi.excel.cell.CellUtil.setCellValue;

/**
 * @author 卢亮宏
 */
public class PoiExcelUtil {

    /**
     * 导出数据
     *
     * @param headMap
     * @param dataList
     */
    public static void exportXlsx(String fileName, String sheetName, Map<String, String> headMap, List<Map<String, Object>> dataList) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(sheetName);


        int rowIndex = 0, columnIndex = 0;
        Set<String> keys = headMap.keySet();

        //表头
        Row row = sheet.createRow(rowIndex++);
        for (String key : keys) {
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(headMap.get(key));
        }

        //内容
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (Map<String, Object> map : dataList) {
                row = sheet.createRow(rowIndex++);
                columnIndex = 0;
                for (String key : keys) {
                    Cell cell = row.createCell(columnIndex++);
                    setCellValue(cell, map.get(key));
                }
            }
        }
        // 折叠列
//        sheet.groupColumn();
        // 折叠行
//        sheet.groupRow();

        OutputStream outputStream;
        try {
            outputStream = Files.newOutputStream(Paths.get(fileName));
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, String> headMap = new HashMap<>();
        headMap.put("a","1");
        headMap.put("b","2");
        headMap.put("c","3");


        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(new HashMap<>(headMap));
        exportXlsx("D:\\test\\test.xlsx","test",headMap,dataList);
    }

}
