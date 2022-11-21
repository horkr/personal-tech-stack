package com.horkr.util.excel;

import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import cn.hutool.poi.excel.WorkbookUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static cn.hutool.poi.excel.cell.CellUtil.setCellValue;

/**
 * @author 卢亮宏
 */
public class PoiExcelUtil {
    public static void writeExcel11(String fileName, Collection<String> heads, Collection<Collection<String>> datas) {
        File file = new File(fileName);
        Workbook book = WorkbookUtil.createBookForWriter(file);
        Sheet one = WorkbookUtil.getOrCreateSheet(book, 0);
//        one.groupColumn(2,5);
        ExcelWriter writer = new ExcelWriter(one).setDestFile(new File(fileName));
        StyleSet styleSet = writer.getStyleSet();
        CellStyle headCellStyle = styleSet.getHeadCellStyle();
        resetHeadCellStyle(headCellStyle);
        CellStyle cellStyle = styleSet.getCellStyle();
        resetCellStyle(cellStyle);
        writer.writeHeadRow(heads);
        for (Collection<String> data : datas) {
            writer.writeRow(data, false);
        }
        one.groupRow(1, 6);
        writer.flush();
    }

    public static void resetHeadCellStyle(CellStyle cellStyle) {
        //设置底边框;
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //设置底边框颜色;
        cellStyle.setBottomBorderColor((short) 0);
        //设置左边框;
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //设置左边框颜色;
        cellStyle.setLeftBorderColor((short) 0);
        //设置右边框;
        cellStyle.setBorderRight(BorderStyle.THIN);
        //设置右边框颜色;
        cellStyle.setRightBorderColor((short) 0);
        //设置顶边框;
        cellStyle.setBorderTop(BorderStyle.THIN);
        //设置顶边框颜色;
        cellStyle.setTopBorderColor((short) 0);
        //设置自动换行;
        cellStyle.setWrapText(true);
        //设置水平对齐的样式为居中对齐;
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setShrinkToFit(true);//设置文本收缩至合适
    }

    public static void fold(File file, Map<Integer, Integer> foldMap) throws Exception {
        Workbook book = WorkbookUtil.createBook(file);
        Sheet one = WorkbookUtil.getOrCreateSheet(book, 0);
        foldMap.forEach(one::groupRow);
        cn.hutool.poi.excel.ExcelWriter writer = one.getLastRowNum() > 1000 ? new BigExcelWriter(one) : new cn.hutool.poi.excel.ExcelWriter(one);
        writer.flush(file);
    }

    public static void resetCellStyle(CellStyle cellStyle) {
        // 背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 字体
        Font headWriteFont = new XSSFFont();
        headWriteFont.setFontName("宋体");//设置字体名字
        headWriteFont.setFontHeightInPoints((short) 14);//设置字体大小
        cellStyle.setFont(headWriteFont); //在样式用应用设置的字体;
        // 样式
        cellStyle.setBorderBottom(BorderStyle.THIN);//设置底边框;
        cellStyle.setBorderLeft(BorderStyle.THIN);  //设置左边框;
        cellStyle.setBorderRight(BorderStyle.THIN);//设置右边框;
        cellStyle.setBorderTop(BorderStyle.THIN);//设置顶边框;
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置水平对齐的样式为居中对齐;
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //设置垂直对齐的样式为居中对齐;
        cellStyle.setShrinkToFit(true);//设置文本收缩至合适
    }

    public static void writeExcel(String fileName, Collection<String> heads, Collection<Collection<String>> datas) {
        ExcelWriter writer = new ExcelWriter(fileName);
        writer.writeHeadRow(heads);
        for (Collection<String> data : datas) {
            writer.writeRow(data, false);
        }
        writer.flush();
    }

    public static void main(String[] args) {
        Collection<String> heads = new ArrayList<>();
        char head = 'c';
        for (int i = 0; i < 10; i++) {
            heads.add(String.valueOf(head));
            head++;
        }
        Collection<Collection<String>> datas = new ArrayList<>();
        for (int i = 2; i < 20; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 1; j < 10; j++) {
                row.add(String.valueOf(i * j));
            }
            datas.add(row);
        }
        writeExcel11("D:\\test\\test5.xlsx", heads, datas);
    }

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


}
