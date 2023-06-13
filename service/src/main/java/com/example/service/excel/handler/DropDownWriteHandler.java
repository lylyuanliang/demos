package com.example.service.excel.handler;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.example.service.excel.annotation.DropdownList;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DropDownWriteHandler implements SheetWriteHandler {

    /**
     * 实体模板类(easy excel 实体类)
     */
    private Class<?> templateClass;

    /**
     * 总行数
     */
    private Integer totalRowSize;

    public DropDownWriteHandler(Class<?> templateClass, Integer totalRowSize) {
        this.templateClass = templateClass;
        this.totalRowSize = totalRowSize;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //动态解决单个单元格下拉框超过255字符
        log.info("第{}个Sheet写入成功。", writeSheetHolder.getSheetNo());
        Sheet workSheet = writeSheetHolder.getSheet();
        DataValidationHelper helper = workSheet.getDataValidationHelper();
        List<String> sortedHeaderList = getSortedHeader(this.templateClass);
        Map<String, String[]> dropdownData = getDropdownData(this.templateClass);
        dropdownData.forEach((fieldName, dataArray) -> {
            if (ObjectUtils.isEmpty(dataArray)) {
                return;
            }

            // 创建sheet，突破下拉框255的限制
            //获取一个workbook
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            //定义sheet的名称
            String sheetName = fieldName + "-DropdownList";
            //1.创建一个隐藏的sheet
            Name name = workbook.getName(sheetName);
            if (ObjectUtils.isEmpty(name)) {
                Sheet sheet = workbook.createSheet(sheetName);
                Name category1Name = workbook.createName();
                category1Name.setNameName(sheetName);
                int length1 = dataArray.length;
                for (int i = 0, length = length1; i < length; i++) {
                    // i:表示你开始的行数  0表示你开始的列数
                    sheet.createRow(i).createCell(0).setCellValue(dataArray[i]);
                }
                if (!ObjectUtils.isEmpty(dataArray)) {
                    //从被创建的sheet第一个单元格开始向下填充  填充到实际数据长度【value.length的行号】
                    category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + (dataArray.length));
                    // sheet设置隐藏
                    workbook.setSheetHidden(workbook.getSheetIndex(sheetName), true);
                }

            } else {
                Name category1Name = workbook.getName(sheetName);
                Sheet sheet1 = workbook.getSheet(sheetName);
                int length1 = dataArray.length;
                for (int i = 0, length = length1; i < length; i++) {
                    // i:表示你开始的行数  0表示你开始的列数
                    sheet1.createRow(i).createCell(0).setCellValue(dataArray[i]);
                }
                if (!ObjectUtils.isEmpty(dataArray)) {
                    category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + (dataArray.length));
                }
            }

            // 获取对应列的下标
            int colNum = sortedHeaderList.indexOf(fieldName);

            //从第一行填充至row行（包含），第 colNum 列至 colNum 列【因为只填充一个单元格,所以起始一致】
            CellRangeAddressList addressList = new CellRangeAddressList(1, totalRowSize, colNum, colNum);
            DataValidationConstraint constraint = helper.createFormulaListConstraint(sheetName);
            setValidation(workSheet, helper, constraint, addressList, "提示", "你输入的值未在备选列表中，请下拉选择合适的值");
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            workSheet.addValidationData(dataValidation);
            //处理Excel兼容性问题
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            Sheet sheet0 = workSheet;
            //5 将刚才设置的sheet引用到你的下拉列表中
            sheet0.addValidationData(dataValidation);
        });

    }

    /**
     * 获取excel表头相关的字段
     *
     * @param clazz 实体模板类(easy excel 实体类)
     * @return
     */
    private List<Field> getExcelField(Class<?> clazz) {
        List<Field> annotatedFields = new ArrayList<>();

        // 遍历实体类的字段，将标注了@ExcelProperty注解的字段统计出来
        for (Field field : clazz.getDeclaredFields()) {
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    /**
     * 获取列顺序
     *
     * @param clazz 实体模板类(easy excel 实体类)
     * @return
     */
    private Map<String, String[]> getDropdownData(Class<?> clazz) {
        List<Field> annotatedFields = getExcelField(clazz);

        return annotatedFields.stream()
                .filter(field -> !field.isAnnotationPresent(ExcelIgnore.class))
                // toMap方法value为null时会NullPointerException, 但我们需要null
                .collect(HashMap::new,
                        (map, field) -> {
                            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                            String key = field.getName();
                            if (excelProperty != null) {
                                key = excelProperty.value()[0];
                            }

                            DropdownList anno = field.getAnnotation(DropdownList.class);
                            String[] value = null;
                            if (anno != null) {
                                value = anno.valueList();
                            }
                            map.put(key, value);
                        },
                        HashMap::putAll);
    }

    /**
     * 按顺序获取excel表头 <br>
     * <b>注意: 实体模板类, 列顺序控制暂时只支持两种情况: 设置order和不设置order</b>
     *
     * @param clazz 实体模板类(easy excel 实体类)
     *              <b>注意: 实体模板类, 列顺序控制暂时只支持两种情况: 设置order和不设置order</b>
     * @return
     */
    private List<String> getSortedHeader(Class<?> clazz) {
        List<Field> annotatedFields = getExcelField(clazz);

        // 根据order属性进行排序
        // 注意: 实体模板类, 列顺序控制暂时只支持两种情况: 设置order和不设置order
        return annotatedFields.stream()
                .sorted(
                        Comparator.comparingInt(f -> f.getAnnotation(ExcelProperty.class).order())
                )
                .map(field -> field.getAnnotation(ExcelProperty.class).value()[0])
                .collect(Collectors.toList());
    }

    /**
     * 设置验证规则
     *
     * @param sheet       sheet对象
     * @param helper      验证助手
     * @param constraint  createExplicitListConstraint
     * @param addressList 验证位置对象
     * @param msgHead     错误提示头
     * @param msgContext  错误提示内容
     */
    private void setValidation(Sheet sheet, DataValidationHelper helper, DataValidationConstraint constraint, CellRangeAddressList addressList, String msgHead, String msgContext) {
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.createErrorBox(msgHead, msgContext);
        sheet.addValidationData(dataValidation);
    }
}