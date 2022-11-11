package com.horkr.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

/**
 * TODO API已过时，需重新开发
 *
 * @create: 2019-07-31 11:18
 **/

@Deprecated
public class EasyExcelUtil {
    private static final Logger logger = LogManager.getLogger(EasyExcelUtil.class);

    private static class DataEventListener extends AnalysisEventListener {
        private Set<Object> resultList;

        @Override
        public void invoke(Object object, AnalysisContext context) {
            if (isNull(resultList)) {
                resultList = new HashSet<>();
            }
            resultList.add(object);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }
    }


    /*

     *@Description: 获取excel导出的数据集

     *@Param: [inputStream, clazz] excel流,每行对应的java类

     *@Return: java.util.Set<java.lang.Object>

     */
//    public static Set<Object> exportedExcelData(InputStream inputStream, Class clazz) {
//        DataEventListener listener = new DataEventListener();
//        ExcelReader reader = EasyExcelFactory.getReader(inputStream, listener);
//        List<Sheet> sheets = reader.getSheets();
//        sheets.forEach(sheet -> {
//            sheet.setClazz(clazz);
//            reader.read(sheet);
//        });
//        return listener.resultList;
//    }
//
//    public static OutputStream writeToOutputStream(OutputStream outputStream, Class clazz, List dataList) {
//        ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
//        Sheet sheet1 = new Sheet(1, 0, clazz);
//        sheet1.setSheetName("第一个sheet");
//        sheet1.setAutoWidth(Boolean.TRUE);
//        writer.write(dataList, sheet1);
//        writer.finish();
//        return outputStream;
//    }
//
//    public static List<Object> readSheetToList(InputStream inputStream,int sheetNo,int startLine) {
//        return EasyExcelFactory.read(inputStream, new Sheet(sheetNo, startLine)).stream().filter(o -> !isNull(o)).collect(Collectors.toList());
//    }


}
