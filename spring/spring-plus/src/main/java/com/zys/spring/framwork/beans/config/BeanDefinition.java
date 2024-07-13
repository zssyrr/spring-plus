package com.zys.spring.framwork.beans.config;

import lombok.Data;

/**
 * @author 郑永帅
 * @since 2024/7/1
 */

@Data
public class BeanDefinition {
    private String beanClassName;
    private String factoryBeanName;
    private boolean lazyInit = false;

}
