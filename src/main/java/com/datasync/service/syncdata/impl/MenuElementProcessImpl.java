package com.datasync.service.syncdata.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.common.MigrateJobParam;
import com.datasync.entity.sourceDTO.TblMenuElement;
import com.datasync.entity.targetDTO.MenuElement;
import com.datasync.service.bo.syncdata.MigrationBaseBO;
import com.datasync.service.bo.syncdata.MigrationMenuElementBO;
import com.datasync.service.dataservice.source.TblMenuElementService;
import com.datasync.service.dataservice.target.MenuElementService;
import com.datasync.service.syncdata.AbstractMigrateData;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Leonpo
 * @date 2023/6/3 14:15
 */
@Slf4j
@Service
public class MenuElementProcessImpl extends AbstractMigrateData<TblMenuElement> {

    @Resource
    private TblMenuElementService tblMenuElementService;

    @Resource
    private MenuElementService menuElementService;

    /**
     * 老数据ID
     *
     * @param source
     * @return
     */
    @Override
    protected Long getOldId(TblMenuElement source) {
        return source.getId();
    }

    /**
     * 根据参数查询旧的数据
     *
     * @param jobParam
     * @return
     */
    @Override
    protected List<TblMenuElement> selectOldListByParam(MigrateJobParam jobParam) {
        LambdaQueryChainWrapper<TblMenuElement> lambdaQuery = tblMenuElementService.lambdaQuery();
        if (StringUtils.isNotBlank(jobParam.getStartTime())) {
            lambdaQuery.ge(TblMenuElement::getUpdateTimeDb, jobParam.getStartTime());
        }
        if (StringUtils.isNotBlank(jobParam.getEndTime())) {
            lambdaQuery.le(TblMenuElement::getUpdateTimeDb, jobParam.getEndTime());
        }
        return lambdaQuery.list();
    }

    @Override
    public MigrationBaseBO getBO(TblMenuElement source) {
        return new MigrationMenuElementBO(source, DsCommonMappingEnum.menuElement, this::selectTargetById);
    }

    public MenuElement selectTargetById(Long id) {
        return menuElementService.lambdaQuery().eq(MenuElement::getId, id).one();
    }

    /**
     * 生成对应的基础数据
     *
     * @param bo
     */
    @Override
    public void createBaseTarget(MigrationBaseBO bo) {
        TblMenuElement source = (TblMenuElement) bo.getSource();
        MenuElement target = (MenuElement) Optional.ofNullable(selectExistsMappingDataId(bo)).orElse(new MenuElement());
        target.setMenuId(dsCommonMappingService.selectMappingMenuId(source.getMenuId()));
        target.setCode(source.getCode());
        target.setIsTab(StringUtils.equals("button", source.getType()));
        target.setName(source.getName());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setIsDeleted(source.getIsDeleted());
        target.setIsEnable(Boolean.TRUE);

        bo.setTarget(target);

    }


    /**
     * 保存目标数据
     *
     * @param bo
     */
    @Override
    public void saveTargetData(MigrationBaseBO bo) {

        menuElementService.saveOrUpdate((MenuElement) bo.getTarget());
        dsCommonMappingService.saveMapping(
                bo.getModuleEnum(),
                ((TblMenuElement) bo.getSource()).getId(),
                ((MenuElement) bo.getTarget()).getId());

    }
}
