package com.datasync.service.syncdata.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.common.MigrateJobParam;
import com.datasync.entity.sourceDTO.TblMenu;
import com.datasync.entity.targetDTO.Menu;
import com.datasync.service.bo.syncdata.MigrationBaseBO;
import com.datasync.service.bo.syncdata.MigrationMenuBO;
import com.datasync.service.syncdata.AbstractMigrateData;
import com.datasync.service.syncdata.MenuService;
import com.datasync.service.syncdata.TblMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Leonpo
 * @date 2023/6/3 11:00
 */
@Slf4j
@Service
public class MenuProcessImpl extends AbstractMigrateData<TblMenu> {


    @Resource
    private TblMenuService tblMenuService;

    @Resource
    private MenuService menuService;

    /**
     * 老数据ID
     *
     * @param source
     * @return
     */
    @Override
    protected Long getOldId(TblMenu source) {
        return source.getMenuId();
    }

    /**
     * 根据参数查询旧的数据
     *
     * @param jobParam
     * @return
     */
    @Override
    protected List<TblMenu> selectOldListByParam(MigrateJobParam jobParam) {
        LambdaQueryChainWrapper<TblMenu> lambdaQuery = tblMenuService.lambdaQuery();
        if (StringUtils.isNotBlank(jobParam.getStartTime())) {
            lambdaQuery.ge(TblMenu::getUpdateTimeDb, jobParam.getStartTime());
        }
        if (StringUtils.isNotBlank(jobParam.getEndTime())) {
            lambdaQuery.le(TblMenu::getUpdateTimeDb, jobParam.getEndTime());
        }
        lambdaQuery.orderByAsc(TblMenu::getLevelNum, TblMenu::getOrderNum, TblMenu::getMenuId);
        return lambdaQuery.list();
    }

    @Override
    public MigrationBaseBO getBO(TblMenu source) {
        return new MigrationMenuBO(source, DsCommonMappingEnum.menu, this::selectTargetById);
    }

    public Menu selectTargetById(Long id) {
        return menuService.lambdaQuery().eq(Menu::getMenuId, id).one();
    }

    /**
     * 生成对应的基础数据
     *
     * @param bo
     */
    @Override
    public void createBaseTarget(MigrationBaseBO bo) {
        TblMenu source = (TblMenu) bo.getSource();
        Menu target = (Menu) Optional.ofNullable(selectExistsMappingDataId(bo)).orElse(new Menu());
        if (Objects.isNull(target.getMenuId())) {
            //菜单首次插入时写入的字段，避免更新的字段
            target.setBusinessId(0L);//根据 2.0一级菜单对应的子系统，再更新一级菜单下所有启用菜单的所属子系统ID
            target.setLevelNum(source.getLevelNum());
            target.setOrderNum(source.getOrderNum());

        }
        target.setCode(source.getCode());
        target.setTitle(source.getTitle());

        if (Objects.nonNull(source.getParentId())) {
            Long mappingParentIdMenuId = dsCommonMappingService.selectMappingMenuId(source.getParentId());
            if (Objects.nonNull(mappingParentIdMenuId)) {
                target.setParentId(mappingParentIdMenuId);
            } else {
                target.setParentId(source.getParentId());
                //没有查询到新的映射关系，后置处理
                ((MigrationMenuBO) bo).addAfterProcess(target, this::afterUpdateMenuParentId);
            }
        }

        target.setIcon(source.getIcon());
        target.setMenuDesc(source.getDescription());
        target.setIsEnable(!source.getIsDisable());
        target.setIsDeleted(source.getIsDeleted());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setType(source.getType());
        target.setIsHidden(source.getHidden());
        target.setName(source.getName());

        bo.setTarget(target);
    }

    private Boolean afterUpdateMenuParentId(Menu target) {
        //查询旧的parentMajorId映射的新ID
        Long mappingMenuId = dsCommonMappingService.selectMappingMenuId(target.getParentId());
        if (Objects.isNull(mappingMenuId)) {
            return Boolean.FALSE;
        }
        return menuService.lambdaUpdate()
                .eq(Menu::getMenuId, target.getMenuId())
                .set(Menu::getParentId, mappingMenuId)
                .update();

    }

    /**
     * 记录一下2.0中剩余未迁移的字段，统一记录到一个临时表
     * 按需重写该方法
     *
     * @param bo
     */
    @Override
    public void remainValues(MigrationBaseBO bo) {

    }

    /**
     * 保存目标数据
     *
     * @param bo
     */
    @Override
    public void saveTargetData(MigrationBaseBO bo) {

        menuService.saveOrUpdate((Menu) bo.getTarget());

        dsCommonMappingService.saveMapping(
                bo.getModuleEnum(),
                ((TblMenu) bo.getSource()).getMenuId(),
                ((Menu) bo.getTarget()).getMenuId());
    }
}
