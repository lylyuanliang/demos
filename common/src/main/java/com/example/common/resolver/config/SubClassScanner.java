package com.example.common.resolver.config;

import com.example.common.resolver.annotation.SubClassAnnotation;
import com.example.common.util.SpringBeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SubClassScanner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 创建一个Map来保存带有特定注解的类
        Map<String, Class<?>> annotatedClasses = new HashMap<>();

        // 获取@ComponentScan注解指定的扫描路径
//        String scanPackage = ClassUtils.getPackageName(ServiceApplication.class);
        // todo 这里写死包路径了, 以后再看有没有更好的办法
        String scanPackage = "com.example";

        // 创建一个ClassPathScanningCandidateComponentProvider实例，并设置扫描路径
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(SubClassAnnotation.class));

        // 扫描指定包下带有特定注解的类
        Set<BeanDefinition> annotatedClassesSet = scanner.findCandidateComponents(scanPackage.trim());

        // 遍历扫描到的类，将其放入Map中
        for (BeanDefinition beanDefinition : annotatedClassesSet) {
            Class<?> annotatedClass = Class.forName(beanDefinition.getBeanClassName());
            SubClassAnnotation annotation = annotatedClass.getAnnotation(SubClassAnnotation.class);
            String name = annotation.type();
            annotatedClasses.put(name, annotatedClass);
        }

        // 将SubClassesRegistry注册为Spring Bean，并注入annotatedClasses
        SubClassesRegistry bean = new SubClassesRegistry(annotatedClasses);
        SpringBeanUtils.registerSingletonBean("subClassesRegistry", bean);

        //将applicationContext转换为ConfigurableApplicationContext
//        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

//        // 获取bean工厂并转换为DefaultListableBeanFactory
//        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext
//                .getBeanFactory();
//
//        // 通过BeanDefinitionBuilder创建bean定义
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
//                .genericBeanDefinition(SubClassesRegistry.class);
//        // 设置属性userAcctDAO,此属性引用已经定义的bean:userAcctDAO
//        beanDefinitionBuilder
//                .addPropertyReference("userAcctDAO", "UserAcctDAO");
//
//        // 注册bean
//        defaultListableBeanFactory.registerBeanDefinition("sdfds",
//                beanDefinitionBuilder.getRawBeanDefinition());


    }
}
