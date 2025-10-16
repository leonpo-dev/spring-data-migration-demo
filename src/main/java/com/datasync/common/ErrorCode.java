package com.datasync.common;

/**
 * 业务错误码封装
 * <p>
 * arch-suggestion
 * 业务相关的错误码，需要根据业务线，子系统划分区间，推荐使用三段式十位字符串错误码。
 * 如错误码1010001000，10表示某业务线，1000表示此业务线内部的子系统，1000表示子系统内部的错误码；
 *
 * @author MoanArch
 * @date 2021-05-07 10:29
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    TENANT_CODE_MAPPING_UN_EXIST("1010002002", "tenantCode=[%s]的映射关系不存在"),

    THERE_ARE_ALSO_OUTSTANDING_RECORDS("1010002030", "还有未处理完成的记录，可能是parentId找不到对应的3.0映射数据，需要人工排查"),

    ;

    private String code;
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}
