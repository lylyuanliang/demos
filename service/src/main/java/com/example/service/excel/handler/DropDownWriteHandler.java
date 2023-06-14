package com.example.service.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;

@Slf4j
public class DropDownWriteHandler extends CustomHandlerBase implements SheetWriteHandler {

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
    public DropDownWriteHandler templateClass(Class<?> templateClass) {
        this.templateClass = templateClass;
        return this;
    }

    /**
     * 设置excel总行数
     *
     * @param totalRowSize 总行数
     * @return
     */
    public DropDownWriteHandler totalRowSize(Integer totalRowSize) {
        this.totalRowSize = totalRowSize;
        return this;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        validate();
        //动态解决单个单元格下拉框超过255字符
        Sheet workSheet = writeSheetHolder.getSheet();
        // 设置下拉框
        createDropdown(workSheet, this.templateClass, this.totalRowSize);
    }

    /**
     * 校验必填参数
     */
    private void validate() {
        if (Objects.isNull(this.templateClass)) {
            throw new RuntimeException("excel模板类型不能为空, 次handler需要借助实体类型, 具体可查找easyExcel编程样例");
        }

        if (Objects.isNull(this.totalRowSize)) {
            log.warn("excel总行数未设置, 将采用默认值[999]");
        }
    }


}