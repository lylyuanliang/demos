## 简介
> 参考[自定义baseMapper示例-gitee](https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-deluxe)

> 参考[自定义baseMapper示例-github](https://github.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-deluxe)

> 自定义自己的通用方法可以实现接口 ISqlInjector 也可以继承抽象类 AbstractSqlInjector 注入通用方法 SQL 语句 然后继承 BaseMapper 添加自定义方法，全局配置 sqlInjector 注入 MP 会自动将类所有方法注入到 mybatis 容器中

## 步骤

> 1. 定义主键生成器, 这里直接实现`org.apache.ibatis.executor.keygen.KeyGenerator`
>   - 其原理是借助`processAfter`方法回查id
>   
> ```
> com.example.injection.IdReturnGenerator
> ```
> 
> 2. 定义sql
>
> ```
> com.example.injection.method.MyInsertWithIdReturn
> ```
>
> 3. 注册, 注册自定义方法
> 
> ```
> com.example.injection.MyInjector
> ```
> 
> 3.1 注入spring容器
> 
> ```
> com.example.injection.config.MybatisPlusConfig
> ```
> 
> 4. 把方法定义到BaseMapper
> 
> ```
> com.example.injection.mapper.MyBaseMapper
> ```
> 

## 坑点
> - 在演示自定义批量和自动填充功能时，需要在mapper方法的参数上定义@Param(), 而mp默认仅支持 list, collection, array 3个命名，不然无法自动填充

> - 实体类setter方法最好不要有返回值(比如lombok注解@Accessors(chain = true)), 否则会找不到set方法
> 代码见: `java.beans.PropertyDescriptor.getWriteMethod`