package com.zys.spring.framwork.webmvc;

import com.zys.spring.framwork.annotation.mvc.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 郑永帅
 * @since 2024/7/4
 */

public class HandlerAdapter {

    public boolean support(Object handler) {
        return handler instanceof HandlerMapping;
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        if (!(handler instanceof HandlerMapping)) {
            return new ModelAndView("500");
        }

        HandlerMapping handlerMapping = (HandlerMapping) handler;
        Method method = handlerMapping.getMethod();
        Parameter[] parameters = method.getParameters();
        Map<String, Class<?>> paramClassMap = new HashMap<>();
        Map<String, Integer> paramIndexMap = new HashMap<>();
        Object[] params = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                String paramName = requestParam.value();
                paramIndexMap.put(paramName, i);

                Class<?> paramType = parameters[i].getType();
                paramClassMap.put(paramName, paramType);
            } else if (parameters[i].getType().equals(HttpServletRequest.class)) {
                params[i] = req;
            } else if (parameters[i].getType().equals(HttpServletResponse.class)) {
                params[i] = resp;
            }
        }

        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!paramIndexMap.containsKey(entry.getKey())) {
                continue;
            }
            String reqParamName = entry.getKey();
            Integer paramIndex = paramIndexMap.get(reqParamName);
            String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            params[paramIndex] = convertParam(value, paramClassMap.get(entry.getKey()));
        }

        method.setAccessible(true);
        try {
            Object methodResult = method.invoke(handlerMapping.getController(), params);
            if (methodResult == null) {
                return null;
            }
            boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
            if (isModelAndView) {
                return (ModelAndView) methodResult;
            }
            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertParam(String value, Class<?> type) {
        if (Objects.isNull(type) || String.class.equals(type)) {
            return value;
        } else if (Integer.class.equals(type)) {
            return Integer.parseInt(value);
        }
        return null;
    }
}
