# 扩展通用

> 参考[官网说明-github](https://github.com/abel533/Mapper/wiki/5.extend)

# 步骤

> 1. 定义provider
> 
> ```com.example.mapperextra.provider.MyInsertProvider```
> 
> 2. 定义自定义接口
> 
> 注意点:
> 
> > 一定注意注解是否正确, 比如 select语句对应`SelectProvider`, insert语句对应`InsertProvider`
> 
> ```com.example.mapperextra.mapper.register.InsertDmWithIdMapper```
> 
> 3. 定义mapper
> 
> ```com.example.mapperextra.mapper.UserDmMapper```

