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
 * 通用映射关系表
 * </p>
 *
 * @author Leonpo
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("ds_common_mapping")
public class DsCommonMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private Long id;

    /**
     * 迁移模块编码
     */
    @TableField("module_code")
    private String moduleCode;

    /**
     * 源对象
     */
    @TableField("source_object")
    private String sourceObject;

    /**
     * 源域
     */
    @TableField("source_field")
    private String sourceField;

    /**
     * 源域值
     */
    @TableField("source_value")
    private String sourceValue;

    /**
     * 目标对象
     */
    @TableField("target_object")
    private String targetObject;

    /**
     * 目标域
     */
    @TableField("target_field")
    private String targetField;

    /**
     * 目标域值
     */
    @TableField("target_value")
    private String targetValue;

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
