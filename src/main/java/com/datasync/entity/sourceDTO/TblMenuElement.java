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
 * 菜单资源表
 * </p>
 *
 * @author system
 * @since 2023-05-16
 */
@Getter
@Setter
@TableName("tbl_menu_element")
public class TblMenuElement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 资源类型(buttton,uri)
     */
    @TableField("type")
    private String type;

    /**
     * 资源名称
     */
    @TableField("name")
    private String name;

    /**
     * 资源路径
     */
    @TableField("uri")
    private String uri;

    /**
     * 请求类型（POST,GET,PUT,DELETE,PAGE）
     */
    @TableField("method")
    private String method;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

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
     * 删除标记(1是，0否)
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 同步编号
     */
    @TableField("sync_id")
    private Long syncId;

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
