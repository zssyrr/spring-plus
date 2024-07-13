package com.zys.spring.framwork.context.support;

import com.zys.spring.framwork.beans.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 郑永帅
 * @since 2024/7/1
 */

public class DefaultListableBeanFactory extends AbstractApplicationContext {
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
