package com.datasync.entity.dto;

import lombok.Data;

/**
 * @author Leonpo
 * @date 2023/6/25 10:19
 */
@Data
public class OldDataDTO {
    /**
     * 数据的ID
     */
    private String businessId;

    /**
     * 操作前数据
     */
    private String before;

    /**
     * 操作类型
     */
    private String changeType;
}
