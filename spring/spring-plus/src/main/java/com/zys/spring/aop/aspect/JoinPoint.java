package com.zys.spring.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author 郑永帅
 * @since 2024/7/11
 */

public interface JoinPoint {
    Method getMethod();

    Object[] getArgs();

    Object getThis();

    Object getUserAttribute(String key);

    void setUserAttributes(String key, Object value);
}
