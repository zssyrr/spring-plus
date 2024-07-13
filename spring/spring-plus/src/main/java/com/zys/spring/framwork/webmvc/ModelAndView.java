package com.zys.spring.framwork.webmvc;

import lombok.Data;

import java.util.Map;

/**
 * @author 郑永帅
 * @since 2024/7/4
 */

@Data
public class ModelAndView {
    /**
     * 页面模板的名称
     */
    private String viewName;
    /**
     * 向页面传送的参数
     */
    private Map<String, ?> model;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
}
