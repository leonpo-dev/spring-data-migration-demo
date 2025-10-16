package com.datasync.entity.dto;

import lombok.Data;

/**
 * @author Leonpo
 * @date 2023/5/18 15:26
 */
@Data
public class TenantDTO {

    /**
     * 源租户ID
     */
    private String sourceTenantCode;

    /**
     * 映射3.0租户ID
     */
    private Long tenantId;

    public TenantDTO(String sourceTenantCode, Long tenantId) {
        this.sourceTenantCode = sourceTenantCode;
        this.tenantId = tenantId;
    }
}
