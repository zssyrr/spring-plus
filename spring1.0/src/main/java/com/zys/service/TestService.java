package com.zys.service;

import com.zys.annotation.biz.Service;

@Service
public class TestService {
    public String getMsg(String input) {
        return "hello" + input;
    }
}
