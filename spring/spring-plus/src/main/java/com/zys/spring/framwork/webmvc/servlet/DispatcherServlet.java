package com.zys.spring.framwork.webmvc.servlet;

import com.zys.spring.framwork.annotation.mvc.Controller;
import com.zys.spring.framwork.annotation.mvc.RequestMapping;
import com.zys.spring.framwork.context.ApplicationContext;
import com.zys.spring.framwork.webmvc.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author 郑永帅
 * @since 2024/6/27
 */

@Slf4j
public class DispatcherServlet extends HttpServlet {
    private ApplicationContext applicationContext;
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final Map<HandlerMapping, HandlerAdapter> handlerAdapterMap = new ConcurrentHashMap<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    private static final String LOCATION = "contextConfigLocation";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("appear error, bro");
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        HandlerMapping handlerMapping = getHandlerMapping(req);
        if (Objects.isNull(handlerMapping)) {
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }

        HandlerAdapter adapter = getHandlerAdapter(handlerMapping);
        if (Objects.isNull(adapter)) {
            processDispatchResult(req, resp, new ModelAndView("404"));
        }
        ModelAndView modelAndView = adapter.handle(req, resp, handlerMapping);

        processDispatchResult(req, resp, modelAndView);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) {
        if (Objects.isNull(modelAndView)) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (ViewResolver viewResolver : viewResolvers) {
            View view = null;
            try {
                view = viewResolver.resolveViewName(modelAndView.getViewName(), null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (Objects.nonNull(view)) {
                try {
                    view.render(modelAndView.getModel(), req, resp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handlerMapping) {
        HandlerAdapter adapter = this.handlerAdapterMap.get(handlerMapping);
        if (Objects.nonNull(adapter) && adapter.support(handlerMapping)) {
            return adapter;
        }
        return null;
    }

    private HandlerMapping getHandlerMapping(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        requestURI = requestURI.replace(contextPath, "").replaceAll("/+", "/");
        for (HandlerMapping handlerMapping : handlerMappings) {
            Pattern pattern = handlerMapping.getPattern();
            if (!pattern.matcher(requestURI).matches()) {
                continue;
            }
            return handlerMapping;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        applicationContext = new ApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(applicationContext);
    }

    private void initStrategies(ApplicationContext applicationContext) {
        initMultipartResolver(applicationContext);

        initLocaleResolver(applicationContext);

        initThemeResolver(applicationContext);

        initHandlerMappings(applicationContext);
        initHandlerAdapters(applicationContext);

        initHandlerExceptionResolver(applicationContext);

        initRequestToViewNameTranslator(applicationContext);

        initViewResolver(applicationContext);

        initFlashMapManager(applicationContext);
    }

    private void initFlashMapManager(ApplicationContext applicationContext) {

    }

    private void initViewResolver(ApplicationContext applicationContext) {
        String templateRoot = applicationContext.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        this.viewResolvers.add(new ViewResolver(templateRootPath));
    }

    private void initRequestToViewNameTranslator(ApplicationContext applicationContext) {

    }

    private void initHandlerExceptionResolver(ApplicationContext applicationContext) {

    }

    private void initHandlerAdapters(ApplicationContext applicationContext) {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapterMap.put(handlerMapping, new HandlerAdapter());
        }
    }

    private void initHandlerMappings(ApplicationContext applicationContext) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            if (Objects.isNull(bean)) {
                continue;
            }
            Class<?> beanClass = bean.getClass();
            if (!beanClass.isAnnotationPresent(Controller.class)) {
                continue;
            }
            String baseUrl = "";
            if (beanClass.isAnnotationPresent(RequestMapping.class)) {
                String url = beanClass.getAnnotation(RequestMapping.class).value();
                if (!url.startsWith("/")) {
                    url = "/" + url;
                }
                baseUrl = url;
            }

            for (Method declaredMethod : beanClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    String methodUrl = declaredMethod.getAnnotation(RequestMapping.class).value();
                    if (!methodUrl.startsWith("/")) {
                        methodUrl = "/" + methodUrl;
                    }

                    String url = baseUrl + methodUrl;
                    String urlRegex = url.replaceAll("\\*", ".*").replaceAll("\\+", "/");
                    Pattern pattern = Pattern.compile(urlRegex);
                    handlerMappings.add(new HandlerMapping(pattern, declaredMethod, bean));
                    log.info("init handlerMapping:{}->{}", urlRegex, declaredMethod);
                }
            }
        }
    }

    private void initThemeResolver(ApplicationContext applicationContext) {

    }

    private void initLocaleResolver(ApplicationContext applicationContext) {

    }

    private void initMultipartResolver(ApplicationContext applicationContext) {

    }
}
