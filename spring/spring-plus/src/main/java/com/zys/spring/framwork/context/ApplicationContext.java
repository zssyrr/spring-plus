package com.zys.spring.framwork.context;

import com.zys.spring.aop.AopConfig;
import com.zys.spring.aop.support.AdvisedSupport;
import com.zys.spring.aop.support.JdkDynamicAopProxy;
import com.zys.spring.framwork.annotation.biz.Service;
import com.zys.spring.framwork.annotation.ioc.Autowired;
import com.zys.spring.framwork.annotation.mvc.Controller;
import com.zys.spring.framwork.beans.BeanWrapper;
import com.zys.spring.framwork.beans.config.BeanDefinition;
import com.zys.spring.framwork.beans.config.BeanPostProcessor;
import com.zys.spring.framwork.context.support.BeanDefinitionReader;
import com.zys.spring.framwork.context.support.DefaultListableBeanFactory;
import com.zys.spring.framwork.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 郑永帅
 * @since 2024/7/1
 */

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {
    private final String[] configLocations;
    private BeanDefinitionReader reader;

    private final Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<>();
    private final Map<String, Object> beanInstanceCache = new ConcurrentHashMap<>();

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            this.refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() throws Exception {
        // 加载配置
        reader = new BeanDefinitionReader(this.configLocations);
        // 扫描类并加载beanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // 注册bean
        registerBeanDefinitions(beanDefinitions);
        // 实例化非懒加载bean
        finishInstantiate();
    }

    private void finishInstantiate() {
        for (Map.Entry<String, BeanDefinition> entry : super.beanDefinitionMap.entrySet()) {
            if (!entry.getValue().isLazyInit()) {
                try {
                    getBean(entry.getKey());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Object getBean(String beanName) {
        Object instance = this.beanInstanceCache.get(beanName);
        if (instance != null) {
            return instance;
        }
        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("beanName is not found");
        }
        BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
        Object o = instantiateBean(beanName, beanDefinition);
        if (o == null) {
            return null;
        }
        beanPostProcessor.postProcessBeforeInitialization(o, beanName);
        BeanWrapper beanWrapper = new BeanWrapper(o);
        this.beanWrapperMap.put(beanName, beanWrapper);
        beanPostProcessor.postProcessAfterInitialization(o, beanName);

        populateBean(o);
        return this.beanWrapperMap.get(beanName).getWrappedInstance();
    }

    private void populateBean(Object o) {
        Class<?> aClass = o.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            String value = field.getAnnotation(Autowired.class).value();
            Object bean;
            // todo 获取bean属性方式？
            if ("".equals(value)) {
                bean = getBean(field.getName());
            } else {
                bean = getBean(value);
            }
            field.setAccessible(true);
            try {
                field.set(o, bean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Object instantiateBean(String beanName, BeanDefinition beanDefinition) {
        Object o = this.beanInstanceCache.get(beanName);
        if (o == null) {
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> aClass = null;
            try {
                aClass = Class.forName(beanClassName);
                if (!aClass.isAnnotationPresent(Controller.class) && !aClass.isAnnotationPresent(Service.class)) {
                    return null;
                }
                o = aClass.newInstance();

                AdvisedSupport support = instantiateAppConfig(beanDefinition);
                support.setTargetClass(aClass);
                support.setTarget(o);
                if (support.pointCutMatch()) {
                    o = createProxy(support);
                }
                this.beanInstanceCache.put(beanName, o);
                this.beanInstanceCache.put(beanClassName, o);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }

    private Object createProxy(AdvisedSupport support) {
        return new JdkDynamicAopProxy(support).getProxy();
    }

    private AdvisedSupport instantiateAppConfig(BeanDefinition beanDefinition) {
        AopConfig aopConfig = new AopConfig();
        aopConfig.setPointCut(reader.getConfig().getProperty("pointCut"));
        aopConfig.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        aopConfig.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        aopConfig.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        aopConfig.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowName"));
        return new AdvisedSupport(aopConfig);
    }

    public Object getBean(Class<?> beanClass) {
        return getBean(beanClass.getName());
    }

    private void registerBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new RuntimeException("beanName" + beanDefinition.getFactoryBeanName() + "重复");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
