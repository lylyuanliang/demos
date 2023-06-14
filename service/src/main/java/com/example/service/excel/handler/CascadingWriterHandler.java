package com.example.service.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Slf4j
public class CascadingWriterHandler extends CustomHandlerBase implements SheetWriteHandler {
    /**
     * 实体模板类(easy excel 实体类)
     */
    private Class<?> templateClass;

    /**
     * 总行数, 默认设置999
     */
    private Integer totalRowSize = 999;


    /**
     * 设置 实体模板类(easy excel 实体类)
     *
     * @param templateClass 实体模板类(easy excel 实体类)
     * @return
     */
    public CascadingWriterHandler templateClass(Class<?> templateClass) {
        this.templateClass = templateClass;
        return this;
    }

    /**
     * 设置excel总行数
     *
     * @param totalRowSize 总行数
     * @return
     */
    public CascadingWriterHandler totalRowSize(Integer totalRowSize) {
        this.totalRowSize = totalRowSize;
        return this;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //获取工作簿
        Sheet sheet = writeSheetHolder.getSheet();

        // 创建所有的主下拉框
        createDropdown(sheet, this.templateClass, this.totalRowSize);

        Workbook book = writeWorkbookHolder.getWorkbook();
        // 创建隐藏sheet, 用来保存级联相关的信息, 顺便创建"名称管理器"
        createSiteSheet(book, this.templateClass);

        // 创建子类列表以及级联关系
        createSubDropdown(sheet, this.templateClass, this.totalRowSize);
    }




}
