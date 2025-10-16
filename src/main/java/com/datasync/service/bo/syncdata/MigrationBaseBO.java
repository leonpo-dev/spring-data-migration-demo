package com.datasync.service.bo.syncdata;


import com.datasync.common.DsCommonMappingEnum;
import com.datasync.entity.targetDTO.ApplyBusiness;
import com.datasync.entity.targetDTO.DsRemainingValue;
import com.datasync.util.Sequence;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Leonpo
 * @date 2023/5/16 17:27
 */
@Data
public abstract class MigrationBaseBO<S, T> {

    protected S source;

    protected T target;

    protected DsCommonMappingEnum moduleEnum;

    protected List<AfterProcessField> afterProcessFieldList = new LinkedList<>();

    /**
     * 根据ID获取映射target的方法
     */
    protected Function<Long, T> selectNewTargetFun;

    public MigrationBaseBO(S source, DsCommonMappingEnum moduleEnum) {
        this.source = source;
        this.moduleEnum = moduleEnum;
    }

    public MigrationBaseBO(S source, DsCommonMappingEnum moduleEnum, Function<Long, T> selectNewTargetFun) {
        this.source = source;
        this.moduleEnum = moduleEnum;
        this.selectNewTargetFun = selectNewTargetFun;
    }

    /**
     * 把数据映射的新的租户ID放到目标数据上，在save db 操作前进行
     */
    public abstract void setTenantIdToTargetData();

    /**
     * 手动set insert数据的主键
     */
    public abstract void setSequenceId();

    /**
     * 周边数据设置ID主键
     *
     * @param remainingValue
     * @param applyBusinessList
     */
    public void setOtherDataSeqId(DsRemainingValue remainingValue, List<ApplyBusiness> applyBusinessList) {
        if (Objects.nonNull(remainingValue) && Objects.isNull(remainingValue.getId())) {
            remainingValue.setId(Sequence.getSEQ());
        }
        if (CollectionUtils.isNotEmpty(applyBusinessList)) {
            applyBusinessList.stream().filter(e -> Objects.isNull(e.getId())).forEach(e -> e.setId(Sequence.getSEQ()));
        }
    }

    /**
     * 设置数据ID，一般用在主表保存后生成对应的数据库ID后，该ID要和关系表的数据做关联
     * 按需重写
     */
    public void setTargetIdToOtherData() {

    }

    public void addAfterProcess(Object object, Function<T, Boolean> afterProcessUpdateService) {

    }

}
