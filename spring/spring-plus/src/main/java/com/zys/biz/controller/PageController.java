package com.zys.biz.controller;

import com.zys.biz.service.QueryService;
import com.zys.spring.framwork.annotation.ioc.Autowired;
import com.zys.spring.framwork.annotation.mvc.Controller;
import com.zys.spring.framwork.annotation.mvc.RequestMapping;
import com.zys.spring.framwork.annotation.mvc.RequestParam;
import com.zys.spring.framwork.webmvc.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 郑永帅
 * @since 2024/7/6
 */

@Controller
public class PageController {
    @Autowired
    private QueryService queryService;

    @RequestMapping("/first.html")
    public ModelAndView query(@RequestParam("teacher") String teacher) {
        String result = queryService.query(teacher);
        Map<String, Object> model = new HashMap<>();
        model.put("teacher", teacher);
        model.put("data", result);
        return new ModelAndView("first.html", model);
    }
}
