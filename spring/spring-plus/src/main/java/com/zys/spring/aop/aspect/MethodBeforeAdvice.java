package com.zys.spring.aop.aspect;

import com.zys.spring.aop.intercepter.MethodInterceptor;
import com.zys.spring.aop.intercepter.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author 郑永帅
 * @since 2024/7/13
 */

public class MethodBeforeAdvice extends AbstractAspectJAdvice implements MethodInterceptor {
    private JoinPoint joinPoint;

    public MethodBeforeAdvice(Method aspectMethod, Object target) {
        super(aspectMethod, target);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.joinPoint = methodInvocation;
        this.invokeAdviceMethod(methodInvocation, null, null);
        return methodInvocation.proceed();
    }
}
