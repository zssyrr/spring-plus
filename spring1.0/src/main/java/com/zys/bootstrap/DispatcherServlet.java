package com.zys.bootstrap;

import com.zys.annotation.biz.Service;
import com.zys.annotation.ioc.Autowired;
import com.zys.annotation.mvc.Controller;
import com.zys.annotation.mvc.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DispatcherServlet extends HttpServlet {
    private final Map<String, Object> objectMapping = new HashMap<>(16);
    private final Map<String, Object> urlMapping = new HashMap<>(16);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(config.getInitParameter("contextConfigLocation"));
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String scanPackage = properties.getProperty("scanPackage");
        String fileDir = scanPackage.replaceAll("\\.", "/");
        packageScan(fileDir);

        for (String className : objectMapping.keySet()) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Service.class)) {
                    Object o = clazz.newInstance();
                    objectMapping.put(className, o);
                } else if (clazz.isAnnotationPresent(Controller.class)) {
                    Object o = clazz.newInstance();
                    objectMapping.put(className, o);
                    String url = "/";
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                        url = requestMapping.value();
                        if (!url.endsWith("/")) {
                            url += "/";
                        }
                    }
                    Method[] declaredMethods = clazz.getDeclaredMethods();
                    for (Method declaredMethod : declaredMethods) {
                        declaredMethod.setAccessible(true);
                        if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping methodRequestMapping = declaredMethod.getAnnotation(RequestMapping.class);
                            String methodUrl = methodRequestMapping.value();
                            if (methodUrl.startsWith("/")) {
                                methodUrl = methodUrl.substring(1, methodUrl.length());
                            }
                            urlMapping.put(url + methodUrl, o);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        for (Object object : objectMapping.values()) {
            Class<?> clazz = object.getClass();
            try {
                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    if (declaredField.isAnnotationPresent(Autowired.class)) {
                        Autowired autowired = declaredField.getAnnotation(Autowired.class);
                        if ("".equals(autowired.value())) {
                            String classType = declaredField.getType().getName();
                            declaredField.set(object, objectMapping.get(classType));
                        } else {
                            String fieldClass = autowired.value();
                            declaredField.set(object, objectMapping.get(fieldClass));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void packageScan(String dir) {
        URL resource = this.getClass().getClassLoader().getResource(dir);
        File file = new File(resource.getFile());
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                String subDir = dir + "/" + subFile.getName();
                packageScan(subDir);
            }
            String[] fileSplit = subFile.getName().split("//.");
            if ("class".equals(fileSplit[1])) {
                objectMapping.put(dir + "." + fileSplit[0], null);
            }
        }
    }
}
