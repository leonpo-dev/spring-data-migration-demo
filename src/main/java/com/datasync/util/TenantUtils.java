package com.datasync.util;


import com.datasync.common.BusinessRuntimeException;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.common.ErrorCode;
import com.datasync.config.SpringBeans;
import com.datasync.entity.dto.TenantDTO;
import com.datasync.service.syncdata.DsCommonMappingService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guanyuanfei
 * @date 2023/5/18 15:13
 */
public class TenantUtils {


    private static Map<String, Long> tenantCodeMap = new ConcurrentHashMap<>();

    private static ThreadLocal<TenantDTO> tenantLocal = new ThreadLocal<>();

    public static void remove() {
        tenantLocal.remove();
    }

    /**
     * 获取映射3.0的租户ID
     *
     * @return
     */
    public static Long getMappingTenantId() {
        return tenantLocal.get().getTenantId();
    }


    /**
     * 获取映射2.0的租户ID
     *
     * @return
     */
    public static String getSourceTenantCode() {
        return tenantLocal.get().getSourceTenantCode();
    }

    /**
     * 根据旧的ID获取映射的新的租户ID
     *
     * @param sourceTenantCode
     * @return
     */
    public static Long getMappingTenantId(String sourceTenantCode) {
        if (!tenantCodeMap.containsKey(sourceTenantCode)) {
            synchronized (TenantUtils.class) {
                if (!tenantCodeMap.containsKey(sourceTenantCode)) {
                    DsCommonMappingService commonMappingService = SpringBeans.getBean(DsCommonMappingService.class);
                    String mappingValue = commonMappingService.selectTargetValueBySourceValue(DsCommonMappingEnum.tenant, sourceTenantCode);
                    if (StringUtils.isNotBlank(mappingValue)) {
                        Long mappingTenantId = Long.valueOf(mappingValue);
                        tenantCodeMap.put(sourceTenantCode, mappingTenantId);
                    } else {
                        ErrorCode unExist = ErrorCode.TENANT_CODE_MAPPING_UN_EXIST;
                        unExist.setMessage(String.format(unExist.getMessage(), sourceTenantCode));
                        throw new BusinessRuntimeException(unExist);
                    }
                }

            }
        }
        Long mappingTenantId = tenantCodeMap.get(sourceTenantCode);
        if (Objects.isNull(tenantLocal.get())) {
            tenantLocal.set(new TenantDTO(sourceTenantCode, mappingTenantId));
        }
        return tenantCodeMap.get(sourceTenantCode);
    }

    /**
     * 根据3.0的ID获取映射的2.0的租户ID
     *
     * @param mappingTenantId
     * @return
     */
    public static String getSourceTenantCode(Long mappingTenantId) {
        if (!tenantCodeMap.values().contains(mappingTenantId)) {
            synchronized (TenantUtils.class) {
                if (!tenantCodeMap.values().contains(mappingTenantId)) {
                    DsCommonMappingService commonMappingService = SpringBeans.getBean(DsCommonMappingService.class);
                    String sourceValue = commonMappingService.selectSourceValueByTargetValue(DsCommonMappingEnum.tenant, mappingTenantId);
                    if (StringUtils.isNotBlank(sourceValue)) {
                        tenantCodeMap.put(sourceValue, mappingTenantId);
                    }
                }
            }
        }
        String sourceTenantCode = tenantCodeMap.entrySet().stream().filter(entity -> Objects.equals(mappingTenantId, entity.getValue())).map(Map.Entry::getKey).findAny().get();
        if (Objects.isNull(tenantLocal.get())) {
            tenantLocal.set(new TenantDTO(sourceTenantCode, mappingTenantId));
        }
        return sourceTenantCode;
    }

}
