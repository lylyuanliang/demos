# 说明

> mybatis-plus的一些扩展点进行记录

# 包结构说明

> - injection: 自定义sql注入器(参考[自定义baseMapper示例](https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-deluxe))
>   - 自定义insert方法, 该方法依赖于数据库id自增, 然后回查id
>     - 场景: 达梦数据库自增主键不支持插入