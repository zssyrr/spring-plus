package com.zys.spring.aop.intercepter;

import com.zys.spring.aop.aspect.JoinPoint;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 郑永帅
 * @since 2024/7/12
 */

@Data
public class MethodInvocation implements JoinPoint {
    private Object proxy;
    private Method method;
    private Object targetObject;
    private Class<?> targetClass;
    private Object[] args;
    private List<MethodInterceptor> interceptors;

    private int currentInterceptorIndex = -1;
    private Map<String, Object> userAttributes = new HashMap<>();

    public MethodInvocation(Object proxy, Method method, Object targetObject, Class<?> targetClass, Object[] args, List<MethodInterceptor> interceptors) {
        this.proxy = proxy;
        this.method = method;
        this.targetObject = targetObject;
        this.targetClass = targetClass;
        this.args = args;
        this.interceptors = interceptors;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArgs() {
        return this.args;
    }

    @Override
    public Object getThis() {
        return targetObject;
    }

    @Override
    public Object getUserAttribute(String key) {
        if (userAttributes == null) {
            return null;
        }
        return userAttributes.get(key);
    }

    public void setUserAttributes(String key, Object value) {
        if (value != null) {
            userAttributes.put(key, value);
        } else {
            userAttributes.remove(key);
        }
    }

    public Object proceed() throws Throwable {
        if (currentInterceptorIndex == this.interceptors.size() - 1) {
            return method.invoke(targetObject, args);
        }
        MethodInterceptor methodInterceptor = this.getInterceptors().get(++currentInterceptorIndex);
        return methodInterceptor.invoke(this);
    }
}
