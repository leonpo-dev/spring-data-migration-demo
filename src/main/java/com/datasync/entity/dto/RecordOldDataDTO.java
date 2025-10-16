package com.datasync.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Leonpo
 * @date 2023/1/13 16:37
 */
@Data
public class RecordOldDataDTO {
    /**
     * Mongo表数据ID
     */
    private String id;

    //医院纬度
    private Long tenantId;

    private String tenantCode;

    private String tenantName;

    /**
     * 记录的code
     */
    private String code;

    /**
     * 记录的名称
     */
    private String name;
    /**
     * 操作描述
     */
    private String desc;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 操作数据
     */
    private List<OldDataDTO> dataDTOS;


    /**
     * 创建事件
     */
    private String createTime;

}
