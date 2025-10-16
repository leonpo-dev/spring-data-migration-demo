package com.datasync.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 静态方式获取 Spring 容器中的 Bean
 */
@Component
public class SpringBeans implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<T> type) {
        if (CONTEXT == null) {
            throw new IllegalStateException("Spring ApplicationContext not initialized yet.");
        }
        return CONTEXT.getBean(type);
    }
}
