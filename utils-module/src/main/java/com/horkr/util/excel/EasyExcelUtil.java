package com.horkr.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.WriteContext;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.handler.context.SheetWriteHandlerContext;
import com.alibaba.excel.write.handler.context.WorkbookWriteHandlerContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteWorkbook;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.horkr.util.dto.DemoData;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 *
 **/

public class EasyExcelUtil {
    private static final Logger log = LogManager.getLogger(EasyExcelUtil.class);


    /**
     * 导出Excel
     * @param outputStream  outputStream
     * @param excelHeads    excel头部行
     * @param excelDatas    excel数据
     * @param foldMarkMap   折叠标记
     */
    private void exportExcel(OutputStream outputStream, List<List<String>> excelHeads, List<List<String>> excelDatas, Map<Integer, Integer> foldMarkMap){
        // 头是头的样式 内容是内容的样式
//        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(ExcelStyleUtil.getHeadStyle(), ExcelStyleUtil.getContentStyle());
        WriteWorkbook writeWorkbook = new WriteWorkbook();
        try {
            writeWorkbook.setOutputStream(outputStream);
            List<WriteHandler> customWriteHandlerList = writeWorkbook.getCustomWriteHandlerList();
//            customWriteHandlerList.add(horizontalCellStyleStrategy);
//            customWriteHandlerList.add(new ExcelWidthhandler());
            writeWorkbook.setHead(excelHeads);
            com.alibaba.excel.ExcelWriter excelWriter = new ExcelWriter(writeWorkbook);
            WriteSheet writeSheet = new WriteSheet();
            writeSheet.setSheetName("Sheet");
            // 写入行数据
            excelWriter.write(excelDatas,writeSheet);
            // 进行折叠
            WriteContext writeContext = excelWriter.writeContext();
            Sheet currentSheet = writeContext.getCurrentSheet();
            foldMarkMap.forEach((start,stop)->{
                // 起始位置+1（excel头）+1(从下一个开始折叠)
                start = start+2;
                // 结束位置+1（excel头）
                stop = stop+1;
                currentSheet.groupRow(start,stop);
            });
            // finish会将数据刷到输出流
            excelWriter.finish();
        } catch (Exception e) {
            log.error("写入excel时出错",e);
        }
    }
}
