package com.datasync.entity.sourceDTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author system
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("tbl_menu")
public class TblMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编号
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单编码
     */
    @TableField("code")
    private String code;

    /**
     * 菜单标题
     */
    @TableField("title")
    private String title;

    /**
     * 所属模块
     */
    @TableField("module_id")
    private Integer moduleId;

    /**
     * 父级菜单编号
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 级别
     */
    @TableField("level_num")
    private Integer levelNum;

    /**
     * 菜单类型（menu:菜单，dirt:目录菜单，page:页面资源）
     */
    @TableField("type")
    private String type;

    /**
     * 资源路径
     */
    @TableField("href")
    private String href;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("order_num")
    private Integer orderNum;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 是否禁用（1是 0否）
     */
    @TableField("is_disable")
    private Boolean isDisable;

    /**
     * 菜单来源
     */
    @TableField("source")
    private String source;

    /**
     * 扩展字段1
     */
    @TableField("attr1")
    private String attr1;

    /**
     * 扩展字段2
     */
    @TableField("attr2")
    private String attr2;

    /**
     * 扩展字段3
     */
    @TableField("attr3")
    private String attr3;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标记(1是 0否)
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 版本号 1旧版 2新版
     */
    @TableField("version")
    private Integer version;

    /**
     * 路由路径
     */
    @TableField("path")
    private String path;

    /**
     * 路由组成
     */
    @TableField("component")
    private String component;

    /**
     * 唯一名称
     */
    @TableField("name")
    private String name;

    /**
     * 是否左侧显示
     */
    @TableField("hidden")
    private Boolean hidden;

    /**
     * 定义栏目选中
     */
    @TableField("meta_active_menu")
    private String metaActiveMenu;

    /**
     * 唯一标识
     */
    @TableField("meta_key")
    private String metaKey;

    /**
     * 重定向路径
     */
    @TableField("redirect")
    private String redirect;

    /**
     * 显示轨迹
     */
    @TableField("view_locus")
    private Boolean viewLocus;

    /**
     * 左侧导航
     */
    @TableField("left_nav")
    private Boolean leftNav;

    /**
     * 是否缓存
     */
    @TableField("is_cache")
    private Boolean isCache;

    /**
     * 路由图标
     */
    @TableField("meta_icon")
    private String metaIcon;

    /**
     * 同步编号
     */
    @TableField("sync_menu_id")
    private Long syncMenuId;

    /**
     * 租户ID
     */
    /*@TableField("tenant_id")
    private Integer tenantId;*/

    /**
     * 最后修改时间-数据库默认写入时间
     */
    @TableField("update_time_db")
    private LocalDateTime updateTimeDb;

}
