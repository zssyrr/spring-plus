package com.zys.spring.aop.support;

import com.zys.spring.aop.AopProxy;

/**
 * @author 郑永帅
 * @since 2024/7/12
 */

public class CglibAopProxy implements AopProxy {
    private AdvisedSupport support;
    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
