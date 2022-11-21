package com.horkr.util.excel;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.write.ExcelBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;

import java.util.Collection;

/**
 * @author 卢亮宏
 */
public class ExtendExcelWriterSheetBuilder extends ExcelWriterSheetBuilder {

    @Override
    public void doWrite(Collection<?> data) {
        super.doWrite(data);
        ExcelWriter excelWriter = (ExcelWriter) ReflectUtil.getFieldValue(this, "excelWriter");
        if (excelWriter == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.write().sheet()' to call this method");
        }
        excelWriter.write(data, build());
        ExcelBuilder excelBuilder = (ExcelBuilder) ReflectUtil.getFieldValue(excelWriter, "excelBuilder");
        excelWriter.finish();
    }
}
