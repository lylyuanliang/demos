package com.example.service.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * 隐藏列
 */
@Slf4j
public class HiddenColWriterHandler extends CustomHandlerBase implements SheetWriteHandler {
    /**
     * 实体模板类(easy excel 实体类)
     */
    private Class<?> templateClass;

    /**
     * 设置 实体模板类(easy excel 实体类)
     *
     * @param templateClass 实体模板类(easy excel 实体类)
     * @return
     */
    public HiddenColWriterHandler templateClass(Class<?> templateClass) {
        this.templateClass = templateClass;
        return this;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //获取工作簿
        Sheet sheet = writeSheetHolder.getSheet();

        List<Integer> hiddenColIndices = getHiddenColIndices(this.templateClass);
        if (hiddenColIndices == null || hiddenColIndices.isEmpty()) {
            return;
        }

        hidden(sheet, hiddenColIndices);
    }




}
