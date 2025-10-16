package com.datasync.service.dataservice.target;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datasync.entity.targetDTO.MenuElement;

/**
 * <p>
 * 菜单资源表 服务类
 * </p>
 *
 * @author system
 * @since 2023-05-16
 */
@DS("targetDB")
public interface MenuElementService extends IService<MenuElement> {

}
