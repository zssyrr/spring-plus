package com.zys.spring.aop.aspect;

import com.zys.spring.aop.intercepter.MethodInterceptor;
import com.zys.spring.aop.intercepter.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author 郑永帅
 * @since 2024/7/13
 */

public class MethodAfterReturningAdvice extends AbstractAspectJAdvice implements MethodInterceptor {
    private JoinPoint joinPoint;

    public MethodAfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.joinPoint = methodInvocation;
        Object result = methodInvocation.proceed();
        invokeAdviceMethod(joinPoint, result, null);
        return result;
    }
}
