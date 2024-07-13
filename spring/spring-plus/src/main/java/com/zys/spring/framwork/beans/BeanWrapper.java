package com.zys.spring.framwork.beans;

import lombok.Data;

/**
 * @author 郑永帅
 * @since 2024/7/1
 */

@Data
public class BeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
