package com.example.service.resolver;

import com.alibaba.fastjson.JSONObject;
import com.example.service.resolver.annotation.CustomRequestBody;
import com.example.service.resolver.config.SubClassesRegistry;
import com.example.service.resolver.util.SpringBeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomParamResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CustomRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest webRequest, WebDataBinderFactory factory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String requestBody = getRequestBody(request);

        Map<String, Object> resultMap = JSONObject.parseObject(requestBody, HashMap.class);

        Object param = createParam(parameter, resultMap);

        return param;
    }

    /**
     * 获取body报文
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getRequestBody(HttpServletRequest request) throws IOException {
        return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * 解析参数
     *
     * @param parameter 方法参数
     * @param paramMap  数据
     * @return
     */
    public Object createParam(MethodParameter parameter, Map<String, Object> paramMap) {
        String typeFileName = "";
        Annotation[] parameterAnnotations = parameter.getParameterAnnotations();
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof CustomRequestBody) {
                typeFileName = ((CustomRequestBody) annotation).typeName();
            }
        }
        SubClassesRegistry subClassesRegistry = SpringBeanUtils.getBean(SubClassesRegistry.class);
        String type = (String) paramMap.get(typeFileName);
        // 获取对应的子类对象
        Class<?> clazz = subClassesRegistry.getSubClasses().get(type);

        if (!Objects.isNull(clazz)) {
            try {
                return convert(paramMap, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("不支持的类型");
    }

    /**
     * 填充属性
     *
     * @param map        参数
     * @param entityType 实体类型
     * @return 填充的了属性的对应实体对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public Object convert(Map<String, Object> map, Class<?> entityType) throws Exception {
        Object entity = entityType.newInstance();
        while (entityType != null) {
            Field[] declaredFields = entityType.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = map.get(fieldName);
                if (value != null) {
                    Object val = convertValue(field.getType(), value);
                    field.set(entity, val);
                }
            }

            entityType = entityType.getSuperclass();
        }

        return entity;
    }

    public static Object convertValue(Class<?> targetType, Object value) {
        // 根据目标类型进行类型转换
        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == int.class || targetType == Integer.class) {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).intValue();
            }
        } else if (targetType == long.class || targetType == Long.class) {
            if (value instanceof String) {
                return Long.parseLong((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        } else if (targetType == double.class || targetType == Double.class) {
            if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            } else if (value instanceof Boolean) {
                return value;
            }
        }

        // 如果无法转换，则返回原始值
        return value;
    }
}

