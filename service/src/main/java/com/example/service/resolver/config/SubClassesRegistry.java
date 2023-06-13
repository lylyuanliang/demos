package com.example.service.resolver.config;

import org.springframework.stereotype.Component;

import java.util.Map;

public class SubClassesRegistry {

    private Map<String, Class<?>> subClasses;

    public SubClassesRegistry(Map<String, Class<?>> subClasses) {
        this.subClasses = subClasses;
    }

    public void setSubClasses(Map<String, Class<?>> subClasses) {
        this.subClasses = subClasses;
    }

    public Map<String, Class<?>> getSubClasses() {
        return subClasses;
    }
}
