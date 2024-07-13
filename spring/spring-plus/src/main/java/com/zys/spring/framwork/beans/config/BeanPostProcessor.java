package com.zys.spring.framwork.beans.config;

/**
 * @author 郑永帅
 * @since 2024/7/2
 */

public class BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
