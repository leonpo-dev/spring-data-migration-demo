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
 * 菜单资源表
 * </p>
 *
 * @author Leonpo
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("uo_menu_element")
public class MenuElement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 所属菜单
     */
    @TableField("menu_id")
    private Long menuId;

    /**
     * 资源编码
     */
    @TableField("code")
    private String code;

    /**
     * 资源名称
     */
    @TableField("name")
    private String name;

    /**
     * 是否tab
     */
    @TableField("is_tab")
    private Boolean isTab;

    /**
     * 是否启用
     */
    @TableField("is_enable")
    private Boolean isEnable;

    /**
     * 来源ID
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 是否逻辑删除:默认0未删除;1已删除
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

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
