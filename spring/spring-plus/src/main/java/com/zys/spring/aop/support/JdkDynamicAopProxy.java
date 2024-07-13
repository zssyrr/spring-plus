package com.zys.spring.aop.support;

import com.zys.spring.aop.AopProxy;
import com.zys.spring.aop.intercepter.MethodInterceptor;
import com.zys.spring.aop.intercepter.MethodInvocation;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author 郑永帅
 * @since 2024/7/12
 */

@Data
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private AdvisedSupport support;

    public JdkDynamicAopProxy(AdvisedSupport support) {
        this.support = support;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.getSupport().getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.getSupport().getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<MethodInterceptor> advices = this.getSupport().getInterceptorAndDynamicInterceptionAdvice(method, this.getSupport().getTargetClass());
        MethodInvocation methodInvocation = new MethodInvocation(proxy, method, support.getTarget(), support.getTargetClass(), args, advices);
        return methodInvocation.proceed();
    }
}
