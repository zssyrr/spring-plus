package com.zys.controller;

import com.zys.annotation.ioc.Autowired;
import com.zys.annotation.mvc.Controller;
import com.zys.annotation.mvc.RequestMapping;
import com.zys.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;


}
