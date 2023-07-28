# 简介
> 参考[自定义baseMapper示例](https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-deluxe)

> 自定义自己的通用方法可以实现接口 ISqlInjector 也可以继承抽象类 AbstractSqlInjector 注入通用方法 SQL 语句 然后继承 BaseMapper 添加自定义方法，全局配置 sqlInjector 注入 MP 会自动将类所有方法注入到 mybatis 容器中

# 步骤

> 1. 定义主键生成器, 这里直接实现`org.apache.ibatis.executor.keygen.KeyGenerator`
>   - 其原理是借助`processAfter`方法回查id
> 2. 定义sql
> 3. 注册, 注册自定义方法
> 4. 把方法定义到BaseMapper

# 坑点
> - 在演示自定义批量和自动填充功能时，需要在mapper方法的参数上定义@Param(), 而mp默认仅支持 list, collection, array 3个命名，不然无法自动填充