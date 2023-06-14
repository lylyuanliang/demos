package com.example.service.excel.demo;

import com.alibaba.excel.EasyExcel;
import com.example.service.excel.handler.CascadingWriterHandler;
import com.example.service.excel.handler.DropDownWriteHandler;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {

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
        DropDownWriteHandler writeHandler = new DropDownWriteHandler()
                .templateClass(Children.class)
                .totalRowSize(dataList.size());
        EasyExcel.write(response.getOutputStream(), Children.class)
                .sheet("sheet1")
                .registerWriteHandler(writeHandler)
                .doWrite(dataList);
    }

    @SneakyThrows
    @GetMapping("/downloadCascade")
    public void downloadCascade(HttpServletResponse response){
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
                .build();

        dataList.add(build);

        // 写出数据
        Class<CascadingVo> clazz = CascadingVo.class;
        CascadingWriterHandler writeHandler = new CascadingWriterHandler()
                .templateClass(clazz)
                .totalRowSize(dataList.size());
        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet("sheet1")
                .registerWriteHandler(writeHandler)
                .doWrite(dataList);
    }
}