package com.zys.spring.framwork.context.support;

import com.zys.spring.framwork.beans.config.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 郑永帅
 * @since 2024/7/2
 */

public class BeanDefinitionReader {
    public static final String SCAN_PACKAGE = "scanPackage";
    private List<String> beanDefinitionClasses = new ArrayList<>();
    private Properties config = new Properties();

    public BeanDefinitionReader(String... configLocations) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configLocations[0].replace("classpath:", ""));
        try {
            config.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        doScanner();
    }

    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (String beanDefinitionClass : beanDefinitionClasses) {
            try {
                Class<?> aClass = Class.forName(beanDefinitionClass);
                if (aClass.isInterface()) {
                    continue;
                }
                String simpleName = aClass.getSimpleName();
                String beanName = toLowerCaseFirst(simpleName);
                BeanDefinition beanDefinition = buildBeanDefinition(beanName, beanDefinitionClass);
                beanDefinitions.add(beanDefinition);

                for (Class<?> anInterface : aClass.getInterfaces()) {
                    String interfaceBeanName = toLowerCaseFirst(anInterface.getSimpleName());
                    beanDefinitions.add(buildBeanDefinition(interfaceBeanName, beanDefinitionClass));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return beanDefinitions;
    }

    private BeanDefinition buildBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private void doScanner() {
        String scanPackage = this.getConfig().getProperty(SCAN_PACKAGE);
        doScan(scanPackage.replaceAll("\\.", "/"));
    }

    private void doScan(String dir) {
        URL resource = this.getClass().getClassLoader().getResource(dir);
        File file = new File(resource.getFile());
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                String subDir = dir + "/" + subFile.getName();
                doScan(subDir);
            }
            String[] fileSplit = subFile.getName().split("\\.");
            if ("class".equals(fileSplit[fileSplit.length - 1])) {
                String classPath = dir.replaceAll("\\/", ".");
                beanDefinitionClasses.add(classPath + "." + subFile.getName().replace(".class", ""));
            }
        }
    }

    private static String toLowerCaseFirst(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return config;
    }
}
