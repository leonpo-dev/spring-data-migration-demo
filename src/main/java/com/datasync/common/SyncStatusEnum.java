package com.datasync.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author guanyuanfei
 * @date 2023/5/18 10:49
 */
@Getter
@AllArgsConstructor
public enum SyncStatusEnum {

    //同步状态：0-未开始;1-进行中；2-成功;3-失败

    NOT_STARTED(0, "未开始"),
    IN_PROGRESS(1, "进行中"),
    SUCCEED(2, "成功"),
    FAIL(3, "失败"),


    ;

    private Integer code;

    private String desc;


    public static SyncStatusEnum match(Integer code) {
        for (SyncStatusEnum value : SyncStatusEnum.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
