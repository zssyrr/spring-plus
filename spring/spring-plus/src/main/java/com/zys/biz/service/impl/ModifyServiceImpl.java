package com.zys.biz.service.impl;

import com.zys.biz.service.ModifyService;
import com.zys.spring.framwork.annotation.biz.Service;

/**
 * @author 郑永帅
 * @since 2024/7/6
 */

@Service
public class ModifyServiceImpl implements ModifyService {
    @Override
    public String add(String name, String addr) {
        return String.format("modifyService add, name=%s, addr=%s",name, addr);
    }

    @Override
    public String edit(Integer id, String name) {
        return String.format("modifyService edit, id=%s, name=%s",id, name);
    }

    @Override
    public String remove(Integer id) {
        return String.format("modifyService remove, id=%s",id);
    }
}
