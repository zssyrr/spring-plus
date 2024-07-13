package com.zys.spring.aop.support;

import com.zys.spring.aop.AopConfig;
import com.zys.spring.aop.aspect.AfterThrowingAdvice;
import com.zys.spring.aop.aspect.MethodAfterReturningAdvice;
import com.zys.spring.aop.aspect.MethodBeforeAdvice;
import com.zys.spring.aop.intercepter.MethodInterceptor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 郑永帅
 * @since 2024/7/11
 */

@Data
public class AdvisedSupport {
    private Class<?> targetClass;
    private Object target;
    private Pattern pointCutClassPattern;

    private transient Map<Method, List<MethodInterceptor>> methodInterceptorMap = new HashMap<>();
    private AopConfig config;

    public AdvisedSupport(AopConfig config) {
        this.config = config;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public List<MethodInterceptor> getInterceptorAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        List<MethodInterceptor> methodInterceptors = methodInterceptorMap.get(method);
        if (methodInterceptors != null) {
            return methodInterceptors;
        }
        try {
            Method targetClassMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
            methodInterceptors = methodInterceptorMap.get(targetClassMethod);
            methodInterceptorMap.put(targetClassMethod, methodInterceptors);
            return methodInterceptors;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean pointCutMatch() {
        return this.pointCutClassPattern.matcher(targetClass.toString()).matches();
    }

    private void parse() {
        String pointCut = config.getPointCut();
        if ("".equals(pointCut)) {
            return;
        }

        pointCut = pointCut.replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");

        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class " + pointCutForClass.substring(pointCutForClass.lastIndexOf(" ") + 1));

        Pattern pattern = Pattern.compile(pointCut);
        try {
            Class<?> aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method declaredMethod : aspectClass.getDeclaredMethods()) {
                aspectMethods.put(declaredMethod.getName(), declaredMethod);
            }

            for (Method declaredMethod : this.getTargetClass().getDeclaredMethods()) {
                String methodString = declaredMethod.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.indexOf("throws") - 1);
                }
                Matcher matcher = pattern.matcher(methodString);
                List<MethodInterceptor> methodInterceptors = new ArrayList<>();
                if (matcher.matches()) {
                    Object aspectObject = aspectClass.newInstance();
                    if (config.getAspectBefore() != null && !config.getAspectBefore().isEmpty()) {
                        methodInterceptors.add(new MethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()), aspectObject));
                    }

                    if (config.getAspectAfter() != null && !config.getAspectAfter().isEmpty()) {
                        methodInterceptors.add(new MethodAfterReturningAdvice(aspectMethods.get(config.getAspectAfter()), aspectObject));
                    }

                    if (config.getAspectAfterThrow() != null && !config.getAspectAfterThrow().isEmpty()) {
                        AfterThrowingAdvice afterThrowingAdvice = new AfterThrowingAdvice(aspectMethods.get(config.getAspectAfterThrow()), aspectObject);
                        afterThrowingAdvice.setThrowingName(config.getAspectAfterThrowingName());
                        methodInterceptors.add(afterThrowingAdvice);
                    }
                    methodInterceptorMap.put(declaredMethod, methodInterceptors);
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
