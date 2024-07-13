package com.zys.spring.aop.intercepter;

/**
 * @author 郑永帅
 * @since 2024/7/11
 */

public interface MethodInterceptor {
    Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
