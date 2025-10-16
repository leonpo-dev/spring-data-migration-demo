package com.datasync.common;

import lombok.Data;

/**
 * @author guanyuanfei
 * @date 2023/5/16 17:19
 */
@Data
public class MigrateJobParam {

    /**
     * 迁移模块
     */
    private String moduleCode;

    /**
     * 源数据租户ID
     */
    protected String sourceTenantCode;

    /**
     * 数据范围开始时间 yyyy-MM-dd
     */
    private String startTime;

    /**
     * 数据范围结束时间  yyyy-MM-dd
     */
    private String endTime;
}
