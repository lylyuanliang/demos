package com.example.service.cases.exceldemo;

import com.alibaba.excel.EasyExcel;
import com.example.common.excel.handler.CascadingWriterHandler;
import com.example.common.excel.handler.DropdownWriteHandler;
import com.example.common.excel.handler.HiddenColWriterHandler;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    /**
     * 下拉测试
     * @param response
     */
    @SneakyThrows
    @GetMapping("/downloadDropdown")
    public void downloadDropdown(HttpServletResponse response){
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("导出模板-级联下拉框", "UTF-8").replaceAll("\\+", "%20");
        // 设置文件名称
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<DropdownVO> dataList = new ArrayList<>();
        DropdownVO build = Children.builder()
                .smallType("重庆市")
                .largeType("重庆市")
                .test("重庆市1111111")
                .build();

        dataList.add(build);

        // 写出数据
        DropdownWriteHandler writeHandler = new DropdownWriteHandler()
                .templateClass(Children.class)
                .totalRowSize(dataList.size());
        EasyExcel.write(response.getOutputStream(), Children.class)
                .sheet("sheet1")
                .registerWriteHandler(writeHandler)
                .doWrite(dataList);
    }

    /**
     * 级联测试
     * @param response
     * @param hiddenType 隐藏列类型, 根据这个类型去控制显示hiddenTypeSub的下拉列表, 具体类型看CascadingVo中的定义
     *                   <p>type1</p>
     *                   <p>type2</p>
     *                   <p>type3</p>
     *
     */
    @SneakyThrows
    @GetMapping("/downloadCascade/{hiddenType}")
    public void downloadCascade(HttpServletResponse response, @PathVariable String hiddenType){
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("导出模板-级联下拉框", "UTF-8").replaceAll("\\+", "%20");
        // 设置文件名称
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<CascadingVo> dataList = new ArrayList<>();
        CascadingVo build = CascadingVo.builder()
                .smallType("重庆市")
                .largeType("重庆市")
                .test("重庆市1111111")
                .test2("1111")
                .hiddenTypeSub("type1-1")
                .other("othner")
                .otherHidden("other")
                .build();

        if (StringUtils.isNotBlank(hiddenType)) {
            build.setHiddenType(hiddenType);
        }

        dataList.add(build);

        // 写出数据
        Class<CascadingVo> clazz = CascadingVo.class;
        CascadingWriterHandler writeHandler = new CascadingWriterHandler()
                .templateClass(clazz)
                .totalRowSize(dataList.size());

        //隐藏handler
        HiddenColWriterHandler hidden = new HiddenColWriterHandler()
                .templateClass(clazz);

        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet("sheet1")
                .registerWriteHandler(writeHandler)
                .registerWriteHandler(hidden)
                .doWrite(dataList);
    }
}