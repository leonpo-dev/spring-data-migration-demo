package com.datasync.service.bo.syncdata;


import com.datasync.common.DsCommonMappingEnum;
import com.datasync.entity.sourceDTO.TblMenu;
import com.datasync.entity.targetDTO.DsRemainingValue;
import com.datasync.entity.targetDTO.Menu;
import com.datasync.util.Sequence;
import com.datasync.util.TenantUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Leonpo
 * @date 2023/6/3 11:02
 */
@Getter
@Setter
public class MigrationMenuBO extends MigrationBaseBO<TblMenu, Menu> {


    /**
     * 剩余需要记录的字段值
     */
    private DsRemainingValue remainingValue;


    public MigrationMenuBO(TblMenu source, DsCommonMappingEnum moduleEnum, Function<Long, Menu> selectNewTargetFun) {
        super(source, moduleEnum, selectNewTargetFun);
    }

    /**
     * 把数据映射的新的租户ID放到目标数据上，在save db 操作前进行
     */
    @Override
    public void setTenantIdToTargetData() {
        target.setTenantId(TenantUtils.getMappingTenantId());
        remainingValue.setTenantId(TenantUtils.getMappingTenantId());
    }

    /**
     * 手动set insert数据的主键
     */
    @Override
    public void setSequenceId() {
        if (Objects.isNull(target.getMenuId())) {
            target.setMenuId(Sequence.getSEQ());
        }
        if (Objects.isNull(remainingValue.getId())) {
            remainingValue.setId(Sequence.getSEQ());
        }
    }

    /**
     * 设置数据ID，一般用在主表保存后生成对应的数据库ID后，该ID要和关系表的数据做关联
     * 按需重写
     */
    @Override
    public void setTargetIdToOtherData() {
        remainingValue.setTargetId(String.valueOf(target.getMenuId()));
    }
}
