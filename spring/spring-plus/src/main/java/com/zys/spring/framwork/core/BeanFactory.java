package com.zys.spring.framwork.core;

/**
 * @author 郑永帅
 * @since 2024/7/1
 */

public interface BeanFactory {
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
