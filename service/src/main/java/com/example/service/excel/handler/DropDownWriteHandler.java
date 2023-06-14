package com.example.service.excel.handler;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.example.service.excel.annotation.DropdownList;
import com.example.service.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
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
        dropDown(workSheet);
    }

    private void dropDown(Sheet sheet){

        List<String> sortedHeaderList = getSortedHeader(this.templateClass);
        Map<String, String[]> dropdownData = getDropdownData(this.templateClass);
        if(MapUtils.isEmpty(dropdownData)){
            return ;
        }
        DataValidationHelper helper = sheet.getDataValidationHelper();
        dropdownData.forEach((fieldName, dataArray) -> {
            if (dataArray == null || dataArray.length == 0) {
                return;
            }
            // 获取对应列的下标
            int colNum = sortedHeaderList.indexOf(fieldName);

            /***起始行、终止行、起始列、终止列**/
            CellRangeAddressList addressList = new CellRangeAddressList(1, totalRowSize, colNum, colNum);
            /***设置下拉框数据**/
            DataValidationConstraint constraint = helper.createExplicitListConstraint(dataArray);
            DataValidation dataValidation = helper.createValidation(constraint, addressList);
            /***处理Excel兼容性问题**/
            if (dataValidation instanceof XSSFDataValidation) {
                dataValidation.setSuppressDropDownArrow(true);
                dataValidation.setShowErrorBox(true);
            } else {
                dataValidation.setSuppressDropDownArrow(false);
            }
            sheet.addValidationData(dataValidation);
        });
    }

    /**
     * 设置隐藏列
     * @param sheet
     */
    private void hidden(Sheet sheet){
//        if (!CollectionUtils.isEmpty(hiddenIndices))
//        {
//            // 设置隐藏列
//            for (Integer hiddenIndex : hiddenIndices) {
//                sheet.setColumnHidden(hiddenIndex, true);
//            }
//        }
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

    /**
     * 获取列顺序
     *
     * @param clazz 实体模板类(easy excel 实体类)
     * @return
     */
    private Map<String, String[]> getDropdownData(Class<?> clazz) {
        // 获取所有字段
        List<Field> allFields = ReflectionUtils.getAllFields(clazz);

        return allFields.stream()
                .filter(field -> !field.isAnnotationPresent(ExcelIgnore.class) && field.isAnnotationPresent(ExcelProperty.class))
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

    private String[] getDropdownDataList(DropdownList anno, List<Field> allFields) {
        String[] valueList = anno.valueList();
        if (!ObjectUtils.isEmpty(valueList)) {
            return valueList;
        }

        String referFiled = anno.cascadingReferFiled();
        if (StringUtils.isBlank(referFiled)) {
            return null;
        }
        Optional<Field> any = allFields.stream()
                .filter(field -> StringUtils.equals(field.getName(), referFiled))
                .findAny();
        if (!any.isPresent()) {
            throw new RuntimeException("字段[" + referFiled + "]不存在");
        }


        DropdownList.Cascading[] cascadings = anno.cascadingValueList();
        return null;
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
        List<Field> annotatedFields = ReflectionUtils.getAllFields(clazz, field -> field.isAnnotationPresent(ExcelProperty.class));

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