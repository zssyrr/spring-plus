package com.zys.controller;

import com.zys.annotation.ioc.Autowired;
import com.zys.annotation.mvc.Controller;
import com.zys.annotation.mvc.RequestMapping;
import com.zys.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping("get")
    public void get(HttpServletRequest req, HttpServletResponse resp, String msg) {
        try {
            resp.getWriter().write(testService.getMsg(msg));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
