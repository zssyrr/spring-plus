package com.zys.biz.service.impl;

import com.zys.biz.service.QueryService;
import com.zys.spring.framwork.annotation.biz.Service;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 郑永帅
 * @since 2024/7/6
 */

@Service
@Slf4j
public class QueryServiceImpl implements QueryService {
    @Override
    public String query(String name) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = format.format(new Date());
        String json = "{\"name\":\"" + name + "\", \"time\":\"" + now + "\"}";
        log.info("query result: {}", json);
        return json;
    }
}
