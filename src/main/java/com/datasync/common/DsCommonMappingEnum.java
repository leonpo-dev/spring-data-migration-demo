package com.datasync.common;


import com.datasync.service.syncdata.DataProcessService;
import com.datasync.service.syncdata.impl.MenuElementProcessImpl;
import com.datasync.service.syncdata.impl.MenuProcessImpl;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author guanyuanfei
 * @date 2023/5/15 16:54
 */
@Getter
public enum DsCommonMappingEnum {

    tenant("tenant", "oldTenant", null, "租户",
            "tenantId", null, null,
            0),

    menu("menu", "tbl_menu", "menu_id", "菜单",
            "uo_menu", "menu_id", MenuProcessImpl.class,
            5300),

    menuElement("menuElement", "tbl_menu_element", "id", "页面元素",
            "uo_menu_element", "id", MenuElementProcessImpl.class,
            5310),


    ;


    /**
     * 迁移模块编码
     */
    private String moduleCode;

    /**
     * 源对象
     */
    private String sourceObject;

    /**
     * 源域
     */
    private String sourceField;

    private String desc;


    /**
     * 目标对象
     */
    private String targetObject;

    /**
     * 目标域
     */
    private String targetField;

    /**
     * 迁移数据服务实现
     */
    private Class<? extends DataProcessService> processService;

    /**
     * 迁移数据处理顺序不可随意修改顺序
     */
    private Integer order;
    /**
     * 老数据记录mongodb的类型
     */
    private DataSyncRecordEnum recordEnum;

    /**
     * 刷新缓存的映射服务
     */
    private CacheRefreshEnum cacheRefreshEnum;

    DsCommonMappingEnum(String moduleCode, String sourceObject, String sourceField, String desc, String targetObject, String targetField, Class<? extends DataProcessService> processService, Integer order) {
        this.moduleCode = moduleCode;
        this.sourceObject = sourceObject;
        this.sourceField = sourceField;
        this.desc = desc;
        this.targetObject = targetObject;
        this.targetField = targetField;
        this.processService = processService;
        this.order = order;
    }

    DsCommonMappingEnum(String moduleCode, String sourceObject, String sourceField, String desc, String targetObject, String targetField, Class<? extends DataProcessService> processService, Integer order, CacheRefreshEnum cacheRefreshEnum) {
        this.moduleCode = moduleCode;
        this.sourceObject = sourceObject;
        this.sourceField = sourceField;
        this.desc = desc;
        this.targetObject = targetObject;
        this.targetField = targetField;
        this.processService = processService;
        this.order = order;
        this.cacheRefreshEnum = cacheRefreshEnum;
    }

    public static DsCommonMappingEnum matchByModuleCode(String moduleCode) {
        if (StringUtils.isBlank(moduleCode)) {
            return null;
        }
        for (DsCommonMappingEnum value : DsCommonMappingEnum.values()) {
            if (StringUtils.equals(value.getModuleCode(), moduleCode)) {
                return value;
            }
        }
        return null;
    }

}
