package com.datasync.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author guanyuanfei
 * @date 2023/5/29 11:38
 */
@Getter
@AllArgsConstructor
public enum DataSyncRecordEnum {

    SYNC_DICTIONARY("SYNC_DICTIONARY", "tbl_property_dictionary", "sync", "字典同步", false),
    ;
    private String code;

    private String name;

    private String module;

    private String desc;

    private Boolean eTFlag;
}
