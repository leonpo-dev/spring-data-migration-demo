package com.datasync.service.bo.syncdata;


import com.datasync.common.DsCommonMappingEnum;
import com.datasync.entity.sourceDTO.TblMenuElement;
import com.datasync.entity.targetDTO.DsRemainingValue;
import com.datasync.entity.targetDTO.MenuElement;
import com.datasync.util.Sequence;
import com.datasync.util.TenantUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author guanyuanfei
 * @date 2023/6/3 14:21
 */
@Getter
@Setter
public class MigrationMenuElementBO extends MigrationBaseBO<TblMenuElement, MenuElement> {


    private DsRemainingValue remainingValue;

    public MigrationMenuElementBO(TblMenuElement source, DsCommonMappingEnum moduleEnum, Function<Long, MenuElement> selectNewTargetFun) {
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
        if (Objects.isNull(target.getId())) {
            target.setId(Sequence.getSEQ());
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
        remainingValue.setTargetId(String.valueOf(target.getId()));
    }
}
