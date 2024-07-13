package com.zys.spring.aop;

/**
 * @author 郑永帅
 * @since 2024/7/12
 */

public interface AopProxy {
    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
