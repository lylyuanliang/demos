package com.example.discoven.controller;

import com.example.discoven.feign.ExcelServiceFeign;
import feign.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@RestController
public class ExcelController {
    @Autowired
    private ExcelServiceFeign excelServiceFeign;

    /**
     * 下拉测试(调用service服务导出)
     */
    @SneakyThrows
    @GetMapping("/downloadDropdown")
    public void downloadDropdown(HttpServletResponse response){
        Response res = excelServiceFeign.downloadDropdown();
        Response.Body body = res.body();
        for(Object key : res.headers().keySet()){
            List<String> kList = (List)res.headers().get(key);
            for(String val : kList){
                response.setHeader(key.toString(), val);
            }
        }

        try (InputStream inputStream = body.asInputStream();
             OutputStream outputStream = response.getOutputStream()) {
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            outputStream.write(b);
            outputStream.flush();
        } catch (IOException e) {
            log.error("导出Excel失败", e);
        }
    }
}
