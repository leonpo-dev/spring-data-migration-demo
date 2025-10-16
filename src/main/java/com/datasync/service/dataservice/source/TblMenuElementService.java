package com.datasync.service.dataservice.source;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datasync.entity.sourceDTO.TblMenuElement;


/**
 * <p>
 * 菜单资源表 服务类
 * </p>
 *
 * @author Leonpo
 * @since 2023-05-16
 */
@DS("sourceDB")
public interface TblMenuElementService extends IService<TblMenuElement> {

}
