package com.zys.spring.aop.aspect;

import com.zys.spring.aop.intercepter.MethodInterceptor;
import com.zys.spring.aop.intercepter.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @author 郑永帅
 * @since 2024/7/13
 */

public class AfterThrowingAdvice extends AbstractAspectJAdvice implements MethodInterceptor {
    private String throwingName;
    private JoinPoint joinPoint;

    public AfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Throwable e) {
            this.invokeAdviceMethod(methodInvocation, null, e);
            throw e;
        }
    }
}
