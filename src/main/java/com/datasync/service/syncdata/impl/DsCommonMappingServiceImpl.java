package com.datasync.service.syncdata.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.entity.targetDTO.DsCommonMapping;
import com.datasync.mapper.targerMapper.DsCommonMappingMapper;
import com.datasync.service.syncdata.DsCommonMappingService;
import com.datasync.util.Sequence;
import com.datasync.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 通用映射关系表 服务实现类
 * </p>
 *
 * @author Leonpo
 * @since 2023-05-16
 */
@Slf4j
@Service
public class DsCommonMappingServiceImpl extends ServiceImpl<DsCommonMappingMapper, DsCommonMapping> implements DsCommonMappingService {


    /**
     * 根据映射类型和源值，获取对应新值的映射关系
     *
     * @param mappingEnum 迁移模块映射对象¬
     * @param sourceValue 源值
     * @return
     */
    @Override
    public DsCommonMapping selectMappingBySourceValue(DsCommonMappingEnum mappingEnum, Object sourceValue) {
        if (ObjectUtils.isEmpty(sourceValue)) return null;
        LambdaQueryChainWrapper<DsCommonMapping> queryChainWrapper = this.lambdaQuery()
                .eq(DsCommonMapping::getModuleCode, mappingEnum.getModuleCode())
                .eq(DsCommonMapping::getSourceObject, mappingEnum.getSourceObject())
                .eq(DsCommonMapping::getSourceValue, sourceValue);
        if (Objects.nonNull(mappingEnum.getSourceField())) {
            queryChainWrapper.eq(DsCommonMapping::getSourceField, mappingEnum.getSourceField());
        }
        List<DsCommonMapping> list = queryChainWrapper.list();
        return list.stream().findFirst().orElse(null);
    }

    /**
     * 根据映射类型和目标值，获取对应新值的映射关系
     *
     * @param mappingEnum
     * @param targetValue
     * @return
     */
    @Override
    public DsCommonMapping selectMappingByTargetValue(DsCommonMappingEnum mappingEnum, Object targetValue) {
        if (ObjectUtils.isEmpty(targetValue)) return null;
        LambdaQueryChainWrapper<DsCommonMapping> queryChainWrapper = this.lambdaQuery()
                .eq(DsCommonMapping::getModuleCode, mappingEnum.getModuleCode())
                .eq(DsCommonMapping::getTargetObject, mappingEnum.getTargetObject())
                .eq(DsCommonMapping::getTargetValue, targetValue);
        if (Objects.nonNull(mappingEnum.getSourceField())) {
            queryChainWrapper.eq(DsCommonMapping::getSourceField, mappingEnum.getSourceField());
        }
        List<DsCommonMapping> list = queryChainWrapper.list();
        return list.stream().findFirst().orElse(null);

    }

    /**
     * 根据映射类型和目标值，获取对应新值的映射关系的源值
     *
     * @param mappingEnum
     * @param targetValue
     * @return
     */
    @Override
    public String selectSourceValueByTargetValue(DsCommonMappingEnum mappingEnum, Object targetValue) {
        if (ObjectUtils.isEmpty(targetValue)) return null;
        return Optional.ofNullable(this.selectMappingByTargetValue(mappingEnum, targetValue)).map(DsCommonMapping::getSourceValue).orElse(null);
    }


    /**
     * 根据映射类型和源值，获取对应新值的映射的值
     *
     * @param mappingEnum
     * @param sourceValue
     * @return
     */
    @Override
    public String selectTargetValueBySourceValue(DsCommonMappingEnum mappingEnum, Object sourceValue) {
        if (ObjectUtils.isEmpty(sourceValue)) return null;
        DsCommonMapping dicMapping = this.selectMappingBySourceValue(mappingEnum, sourceValue);
        return Optional.ofNullable(dicMapping).map(e -> e.getTargetValue()).orElse(null);
    }


    /**
     * 保存新旧映射关系
     *
     * @param mappingEnum
     * @param sourceValue
     * @param targetValue
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMapping(DsCommonMappingEnum mappingEnum, Object sourceValue, Object targetValue) {
        boolean exists = this.lambdaQuery().eq(DsCommonMapping::getModuleCode, mappingEnum.getModuleCode())
                .eq(DsCommonMapping::getSourceObject, mappingEnum.getSourceObject())
                .eq(DsCommonMapping::getSourceValue, sourceValue)
                .eq(DsCommonMapping::getTargetObject, mappingEnum.getTargetObject())
                .eq(DsCommonMapping::getTargetValue, targetValue)
                .exists();
        if (exists) {
            log.warn("mappingEnum=[{}],sourceValue=[{}],targetValue=[{}]已存在映射关系，不维护了", mappingEnum, sourceValue, targetValue);
            return;
        }
        DsCommonMapping mapping = new DsCommonMapping();


        mapping.setId(Sequence.getSEQ());
        mapping.setModuleCode(mappingEnum.getModuleCode());
        mapping.setSourceObject(mappingEnum.getSourceObject());
        mapping.setSourceField(mappingEnum.getSourceField());
        mapping.setSourceValue(String.valueOf(sourceValue));
        mapping.setTargetObject(mappingEnum.getTargetObject());
        mapping.setTargetField(mappingEnum.getTargetField());
        mapping.setTargetValue(String.valueOf(targetValue));
        mapping.setTenantId(TenantUtils.getMappingTenantId());


        this.save(mapping);
    }


    /**
     * 根据2.0的菜单Id查询出映射的新的ID
     *
     * @param oldMenuId
     * @return
     */
    @Override
    public Long selectMappingMenuId(Long oldMenuId) {
        DsCommonMapping menuMapping = this.selectMappingBySourceValue(DsCommonMappingEnum.menu, oldMenuId);
        return Optional.ofNullable(menuMapping).map(e -> Long.valueOf(e.getTargetValue())).orElse(null);
    }


}
