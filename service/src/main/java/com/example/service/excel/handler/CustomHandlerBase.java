package com.example.service.excel.handler;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.example.service.excel.annotation.ColHidden;
import com.example.service.excel.annotation.DropdownList;
import com.example.service.util.ReflectionUtils;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class CustomHandlerBase {
    private static final String ELSE_CASCADING_SUB_DEFAULT = "__else_cascading_sub_default__";
    /**
     * 从实体模板中获取下拉框的列表
     *
     * @param clazz 实体模板类(easy excel 实体类)
     * @return
     */
    public Map<String, String[]> getDropdownData(Class<?> clazz) {
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

    /**
     * 获取模板中所有的级联信息
     *
     * @param clazz 模板类
     * @return
     */
    public List<CascadingBean> getCascadingInfo(Class<?> clazz) {
        // 获取所有字段
        List<Field> allFields = ReflectionUtils.getAllFields(clazz);
        return allFields.stream()
                .filter(field -> {
                            DropdownList annotation = field.getAnnotation(DropdownList.class);
                            if (annotation == null) {
                                return false;
                            }
                            String referFiled = annotation.cascadingReferFiled();
                            // 这里认为指定了 cascadingReferFiled , 就表示此字段为级联的子列表
                            return StringUtils.isNotBlank(referFiled);
                        }
                )
                .map(field -> {
                    DropdownList anno = field.getAnnotation(DropdownList.class);
                    String referFiledName = anno.cascadingReferFiled();
                    Optional<Field> any = allFields.stream()
                            .filter(f -> f.getName().equals(referFiledName))
                            .findAny();
                    if (!any.isPresent()) {
                        throw new RuntimeException("[" + referFiledName + "]对应的字段不存在");
                    }
                    Field referField = any.get();
                    DropdownList referDropdownAnno = referField.getAnnotation(DropdownList.class);
                    // 获取主列表
                    String[] majorCategory = referDropdownAnno.valueList();
                    if (majorCategory == null || majorCategory.length == 0) {
                        throw new RuntimeException("[" + referFiledName + "]对应的主列表未设置, 本级联关系将不生效");
                    }

                    Map<String, String[]> subCategoryMap = new HashMap<>();
                    DropdownList.Cascading[] cascadingArray = anno.cascadingValueList();
                    for (DropdownList.Cascading cascading : cascadingArray) {
                        String key = cascading.key();
                        String[] valueList = cascading.valueList();

                        subCategoryMap.put(key, valueList);
                    }

                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    String[] headerName = excelProperty.value();
                    ExcelProperty referExcelProperty = referField.getAnnotation(ExcelProperty.class);
                    String[] referHeaderName = referExcelProperty.value();

                    return CascadingBean.builder()
                            .majorCategoryList(majorCategory)
                            .subCategoryMap(subCategoryMap)
                            .headerName(headerName[0])
                            .referHeaderName(referHeaderName[0])
                            .build();
                }).collect(Collectors.toList());
    }

    /**
     * 按顺序获取excel表头 <br>
     * <b>注意: 实体模板类, 列顺序控制暂时只支持两种情况: 设置order和不设置order</b>
     *
     * @param clazz 实体模板类(easy excel 实体类)
     *              <b>注意: 实体模板类, 列顺序控制暂时只支持两种情况: 设置order和不设置order</b>
     * @return
     */
    public List<String> getSortedHeader(Class<?> clazz) {
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
     * 获取所有需要隐藏的列的下标集合
     *
     * @param clazz 实体模板类(easy excel 实体类)
     * @return
     */
    public List<Integer> getHiddenColIndices(Class<?> clazz) {
        List<Field> allFields = ReflectionUtils.getAllFields(clazz, field -> field.isAnnotationPresent(ExcelProperty.class));

        List<String> sortedHeaders = getSortedHeader(clazz);

        return allFields.stream()
                .filter(field -> field.isAnnotationPresent(ColHidden.class))
                .map(field -> sortedHeaders.indexOf(field.getAnnotation(ExcelProperty.class).value()[0]))
                .collect(Collectors.toList());
    }

    /**
     * 设置下拉框
     *
     * @param sheet 工作表
     */
    public void createDropdown(Sheet sheet, Class<?> templateClass, Integer totalRowSize) {
        if (totalRowSize == null || totalRowSize == 0) {
            // 至少处理一行
            totalRowSize =1;
        }

        List<String> sortedHeaderList = getSortedHeader(templateClass);
        Map<String, String[]> dropdownData = getDropdownData(templateClass);
        if (MapUtils.isEmpty(dropdownData)) {
            return;
        }
        DataValidationHelper helper = sheet.getDataValidationHelper();
        Integer finalTotalRowSize = totalRowSize;
        dropdownData.forEach((headerName, dataArray) -> {
            if (dataArray == null || dataArray.length == 0) {
                return;
            }
            // 获取对应列的下标
            int colNum = sortedHeaderList.indexOf(headerName);

            /***起始行、终止行、起始列、终止列**/
            CellRangeAddressList addressList = new CellRangeAddressList(1, finalTotalRowSize, colNum, colNum);
            /***设置下拉框数据**/
            DataValidationConstraint constraint = helper.createExplicitListConstraint(dataArray);
            setValidation(sheet, helper, constraint, addressList, "提示", "你输入的值未在备选列表中，请下拉选择合适的值");
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
     * 设置级联子下拉框
     *
     * @param sheet 工作表
     */
    public void createSubDropdown(Sheet sheet, Class<?> templateClass, Integer totalRowSize) {
        if (totalRowSize == null || totalRowSize == 0) {
            // 至少处理一行
            totalRowSize =1;
        }

        List<String> sortedHeaderList = getSortedHeader(templateClass);
        List<CascadingBean> cascadingInfo = getCascadingInfo(templateClass);
        if (ObjectUtils.isEmpty(cascadingInfo)) {
            return;
        }
        DataValidationHelper helper = sheet.getDataValidationHelper();
        Integer finalTotalRowSize = totalRowSize;
        cascadingInfo.forEach(bean -> {
            String headerName = bean.getHeaderName();
            String referHeaderName = bean.getReferHeaderName();

            int colNum = sortedHeaderList.indexOf(headerName);
            int referColNum = sortedHeaderList.indexOf(referHeaderName);

            String letter = convertIndexToColumnLetter(referColNum + 1);
            // 小类规则(各单元格按个设置)
            // "INDIRECT($A$" + 2 + ")" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，如果A2是浙江省，那么此处就是浙江省下面的市
            // 为了让每个单元格的公式能动态适应，使用循环挨个给公式。
            // 循环几次，就有几个单元格生效，次数要和上面的大类影响行数一一对应，要不然最后几个没对上的单元格实现不了级联
            for (Integer i = 1; i < finalTotalRowSize + 1; i++) {

                CellRangeAddressList rangeAddressList = new CellRangeAddressList(i, i, colNum, colNum);
                int referRow = i + 1;
                // 改用相对引用, 注意$表示绝对引用,例如INDIRECT($A$2), INDIRECT(A2)就表示相对引用
                DataValidationConstraint formula = helper.createFormulaListConstraint("INDIRECT(" + letter + referRow + ")");
                setValidation(sheet, helper, formula, rangeAddressList, "提示", "你输入的值未在备选列表中，请下拉选择合适的值");
            }

        });
    }

    /**
     * 创建隐藏sheet, 用来保存级联相关的信息, 顺便创建"名称管理器"
     *
     * @param book          工作book
     * @param templateClass 模板类
     */
    public void createSiteSheet(Workbook book, Class<?> templateClass) {
        List<CascadingBean> cascadingInfo = getCascadingInfo(templateClass);
        if (ObjectUtils.isEmpty(cascadingInfo)) {
            return;
        }
        //创建一个专门用来存放地区信息的隐藏sheet页
        //不能在现实页之前创建，否则无法隐藏。
        Sheet hideSheet = book.createSheet("site");
        book.setSheetHidden(book.getSheetIndex(hideSheet), true);
        AtomicInteger rowId = new AtomicInteger();
        cascadingInfo.forEach(cascadingBean -> {
            String[] majorCategoryList = cascadingBean.getMajorCategoryList();
            Map<String, String[]> subCategoryMap = cascadingBean.getSubCategoryMap();

            // 以下代码copy from website, 懂的都懂
            // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
            Row proviRow = hideSheet.createRow(rowId.getAndIncrement());
            proviRow.createCell(0).setCellValue("大类列表" + rowId);
            for (int i = 0; i < majorCategoryList.length; i++) {
                Cell proviCell = proviRow.createCell(i + 1);
                String key = majorCategoryList[i];
                proviCell.setCellValue(key);

                String[] son = subCategoryMap.get(key);
                if (son == null) {
                    son = subCategoryMap.get(ELSE_CASCADING_SUB_DEFAULT);
                }

                Row row = hideSheet.createRow(rowId.getAndIncrement());
                row.createCell(0).setCellValue(key);
                for (int j = 0; j < son.length; j++) {
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(son[j]);
                }

                // 添加名称管理器
                String range = getRange(1, rowId.get(), son.length);
                Name name = book.createName();
                name.setNameName(key);
                String formula = "site!" + range;
                name.setRefersToFormula(formula);
            }
        });
    }

    /**
     * 设置隐藏列
     *
     * @param sheet         工作sheet
     * @param hiddenIndices 需要隐藏的列索引集合
     */
    public void hidden(Sheet sheet, List<Integer> hiddenIndices) {
        if (!CollectionUtils.isEmpty(hiddenIndices)) {
            // 设置隐藏列
            for (Integer hiddenIndex : hiddenIndices) {
                sheet.setColumnHidden(hiddenIndex, true);
            }
        }
    }

    /**
     * 将数字下标转成字母列标
     *
     * @param index 数字下表(从1开始)
     * @return
     */
    public String convertIndexToColumnLetter(int index) {
        StringBuilder columnLetter = new StringBuilder();

        while (index > 0) {
            int remainder = (index - 1) % 26;
            char letter = (char) (65 + remainder); // 将 ASCII 码转换为字母
            columnLetter.insert(0, letter);
            index = (index - 1) / 26;
        }

        return columnLetter.toString();
    }

    /**
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     * @author denggonghai 2016年8月31日 下午5:17:49
     */
    public String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
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
    public void setValidation(Sheet sheet, DataValidationHelper helper, DataValidationConstraint constraint, CellRangeAddressList addressList, String msgHead, String msgContext) {
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.createErrorBox(msgHead, msgContext);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 级联相关信息
     */
    @Data
    @Builder
    public static class CascadingBean {
        /**
         * 大类列表
         */
        private String[] majorCategoryList;
        /**
         * 小类列表
         * key对应大类中的每一个元素
         * value对应子列表
         */
        private Map<String, String[]> subCategoryMap;

        /**
         * 当前字段对应的表头
         */
        private String headerName;

        /**
         * 关联字段对应的表头(级联关系中上级表头名称)
         */
        private String referHeaderName;
    }
}
