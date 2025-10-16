package com.datasync.service.syncdata;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datasync.entity.sourceDTO.TblMenu;


/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author system
 * @since 2023-05-16
 */
@DS("hoecommon")
public interface TblMenuService extends IService<TblMenu> {

}
