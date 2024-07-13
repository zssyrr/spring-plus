package com.zys.biz.aop.aspect;

import com.zys.spring.aop.aspect.JoinPoint;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 郑永帅
 * @since 2024/7/13
 */

@Slf4j
public class LogAspect {
    public void before(JoinPoint joinPoint) {
        joinPoint.setUserAttributes("startTime", System.currentTimeMillis());
        log.info("before method");
    }

    public void after(JoinPoint joinPoint) {
        joinPoint.setUserAttributes("endTime", System.currentTimeMillis());
        log.info("after method");
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("afterThrowing method");
    }
}
