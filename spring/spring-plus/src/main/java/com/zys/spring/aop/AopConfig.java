package com.zys.spring.aop;

import lombok.Data;

/**
 * @author 郑永帅
 * @since 2024/7/11
 */

@Data
public class AopConfig {
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;

}
