package com.datasync.service.syncdata;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.entity.targetDTO.DsCommonMapping;

/**
 * <p>
 * 通用映射关系表 服务类
 * </p>
 *
 * @author Leonpo
 * @since 2023-05-16
 */
public interface DsCommonMappingService extends IService<DsCommonMapping> {

    /**
     * 根据映射类型和源值，获取对应新值的映射关系
     *
     * @param mappingEnum 迁移模块映射对象¬
     * @param sourceValue 源值
     * @return
     */
    DsCommonMapping selectMappingBySourceValue(DsCommonMappingEnum mappingEnum, Object sourceValue);

    /**
     * 根据映射类型和目标值，获取对应新值的映射关系
     *
     * @param mappingEnum
     * @param targetValue
     * @return
     */
    DsCommonMapping selectMappingByTargetValue(DsCommonMappingEnum mappingEnum, Object targetValue);


    /**
     * 根据映射类型和目标值，获取对应新值的映射关系的源值
     *
     * @param mappingEnum
     * @param targetValue
     * @return
     */
    String selectSourceValueByTargetValue(DsCommonMappingEnum mappingEnum, Object targetValue);


    /**
     * 根据映射类型和源值，获取对应新值的映射的值
     *
     * @param mappingEnum
     * @param sourceValue
     * @return
     */
    String selectTargetValueBySourceValue(DsCommonMappingEnum mappingEnum, Object sourceValue);


    /**
     * 保存新旧映射关系
     *
     * @param mappingEnum
     * @param sourceValue
     * @param targetValue
     */
    void saveMapping(DsCommonMappingEnum mappingEnum, Object sourceValue, Object targetValue);


    /**
     * 根据2.0的菜单Id查询出映射的新的ID
     *
     * @param oldMenuId
     * @return
     */
    Long selectMappingMenuId(Long oldMenuId);


}
