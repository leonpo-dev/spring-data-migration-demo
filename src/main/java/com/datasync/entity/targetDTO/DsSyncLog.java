package com.datasync.entity.targetDTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 迁移记录表
 * </p>
 *
 * @author system
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("ds_sync_log")
public class DsSyncLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private Long id;

    /**
     * 迁移模块编码
     */
    @TableField("module_code")
    private String moduleCode;

    /**
     * 调用类型：job/http
     */
    @TableField("call_type")
    private String callType;

    /**
     * 同步状态：0-未开始;1-进行中；2-成功;3-失败
     */
    @TableField("sync_status")
    private Integer syncStatus;

    /**
     * 业务唯一键
     */
    @TableField("unique_key")
    private String uniqueKey;

    /**
     * 相关参数
     */
    @TableField("param")
    private String param;

    /**
     * 异常信息
     */
    @TableField("exception_msg")
    private String exceptionMsg;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 创建人ID
     */
    @TableField("create_userid")
    private Long createUserid;

    /**
     * 创建人姓名
     */
    @TableField("create_username")
    private String createUsername;

    /**
     * 创建人IP
     */
    @TableField("create_userip")
    private String createUserip;

    /**
     * 创建时间-应用操作时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建时间-数据库操作时间
     */
    @TableField("create_time_db")
    private LocalDateTime createTimeDb;

    /**
     * 最后修改人ID
     */
    @TableField("update_userid")
    private Long updateUserid;

    /**
     * 最后修改人姓名
     */
    @TableField("update_username")
    private String updateUsername;

    /**
     * 最后修改人IP
     */
    @TableField("update_userip")
    private String updateUserip;

    /**
     * 最后修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 最后修改时间-数据库默认写入时间
     */
    @TableField("update_time_db")
    private LocalDateTime updateTimeDb;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;


}
