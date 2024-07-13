package com.zys.biz.service;

/**
 * @author 郑永帅
 * @since 2024/7/6
 */

public interface ModifyService {
    String add(String name, String addr);

    String edit(Integer id, String name);

    String remove(Integer id);
}
