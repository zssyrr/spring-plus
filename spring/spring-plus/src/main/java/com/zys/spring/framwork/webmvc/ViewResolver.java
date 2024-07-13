package com.zys.spring.framwork.webmvc;

import java.io.File;
import java.util.Locale;

/**
 * @author 郑永帅
 * @since 2024/7/6
 */

public class ViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";
    private File templateRootDir;

    public ViewResolver(String templateRoot) {
//        templateRoot = "C:\\java\\projects\\spring\\spring-plus\\out\\artifacts\\spring_plus_war_exploded\\WEB-INF\\classes\\static";
//        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRoot);
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (viewName == null || viewName.isEmpty()) {
            return null;
        }
        if (!viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX)) {
            viewName = viewName + DEFAULT_TEMPLATE_SUFFIX;
        }
        String viewPath = templateRootDir.getPath() + "\\" + viewName;
        return new View(new File(viewPath));
    }
}
