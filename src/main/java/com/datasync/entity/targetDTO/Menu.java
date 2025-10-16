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
 * 菜单表
 * </p>
 *
 * @author Leonpo
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("uo_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId("menu_id")
    private Long menuId;

    /**
     * 菜单编码
     */
    @TableField("code")
    private String code;

    /**
     * 所属业务
     */
    @TableField("business_id")
    private Long businessId;

    /**
     * 父级菜单，一级菜单的parentId=businessId
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 级别
     */
    @TableField("level_num")
    private Integer levelNum;

    /**
     * 排序
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 菜单标题
     */
    @TableField("title")
    private String title;

    /**
     * 自定义标题
     */
    @TableField("custom_title")
    private String customTitle;

    /**
     * 唯一名称
     */
    @TableField("name")
    private String name;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 是否显示4级路由
     */
    @TableField("is_submenu")
    private Boolean isSubmenu;

    /**
     * 是否登录白名单
     */
    @TableField("is_white_list")
    private Boolean isWhiteList;

    /**
     * 是否最小权限包
     */
    @TableField("is_package")
    private Boolean isPackage;

    /**
     * 是否左侧显示
     */
    @TableField("is_hidden")
    private Boolean isHidden;

    /**
     * 高亮菜单
     */
    @TableField("active_menu")
    private String activeMenu;

    /**
     * 应用平台（web,app）
     */
    @TableField("platform")
    private String platform;

    /**
     * 菜单描述
     */
    @TableField("menu_desc")
    private String menuDesc;

    /**
     * 是否启用
     */
    @TableField("is_enable")
    private Boolean isEnable;

    /**
     * 是否自定义编辑
     */
    @TableField("is_custom")
    private Boolean isCustom;

    /**
     * 来源ID
     */
    @TableField("source_id")
    private Long sourceId;

    /**
     * 2.0菜单type
     */
    @TableField("type")
    private String type;

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
