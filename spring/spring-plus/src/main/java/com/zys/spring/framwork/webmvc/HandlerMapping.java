package com.zys.spring.framwork.webmvc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author 郑永帅
 * @since 2024/7/4
 */

@Data
@AllArgsConstructor
public class HandlerMapping {
    private Pattern pattern;
    private Method method;
    private Object controller;

}
