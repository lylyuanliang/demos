package com.example.injection;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.example.injection.method.MyInsertWithIdReturn;

import java.util.List;

/**
 * 自定义方法注册
 */
public class MyInjector extends DefaultSqlInjector {
    /**
     * 如果只需增加方法，保留MP自带方法
     * 可以super.getMethodList() 再add
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        // 保留原有的method
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new MyInsertWithIdReturn());
        return methodList;
    }
}
