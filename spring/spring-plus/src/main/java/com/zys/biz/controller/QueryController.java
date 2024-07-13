package com.zys.biz.controller;

import com.zys.biz.service.ModifyService;
import com.zys.biz.service.QueryService;
import com.zys.spring.framwork.annotation.ioc.Autowired;
import com.zys.spring.framwork.annotation.mvc.Controller;
import com.zys.spring.framwork.annotation.mvc.RequestMapping;
import com.zys.spring.framwork.annotation.mvc.RequestParam;
import com.zys.spring.framwork.webmvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 郑永帅
 * @since 2024/7/6
 */

@Controller
@RequestMapping("/biz")
public class QueryController {
    @Autowired
    private QueryService queryService;
    @Autowired
    private ModifyService modifyService;

    @RequestMapping("/query")
    public ModelAndView query(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name) {
        String result = queryService.query(name);
        return output(response, result);
    }

    @RequestMapping("/add*")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name, @RequestParam("addr") String addr) {
        String result = modifyService.add(name, addr);
        return output(response, result);
    }

    private ModelAndView output(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
