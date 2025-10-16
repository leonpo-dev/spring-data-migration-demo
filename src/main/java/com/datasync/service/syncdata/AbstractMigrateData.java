package com.datasync.service.syncdata;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.datasync.common.DataChangeType;
import com.datasync.common.DataSyncRecordEnum;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.common.MigrateJobParam;
import com.datasync.entity.dto.OldDataDTO;
import com.datasync.entity.dto.RecordOldDataDTO;
import com.datasync.entity.targetDTO.DsCommonMapping;
import com.datasync.mongo.RecordMongoDAO;
import com.datasync.service.ConvertObj;
import com.datasync.service.bo.syncdata.AfterProcessField;
import com.datasync.service.bo.syncdata.MigrationBaseBO;
import com.datasync.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


@Slf4j
public abstract class AbstractMigrateData<S> implements DataProcessService<S>, ConvertObj {

    @Resource
    protected DsCommonMappingService dsCommonMappingService;

    @Resource
    protected TransactionTemplate transactionTemplate;
    @Resource
    private RecordMongoDAO recordMongoDao;


    /**
     * 老数据ID
     *
     * @return
     */
    protected abstract Object getOldId(S source);

    /**
     * 根据参数查询旧的数据
     *
     * @param jobParam
     * @return
     */
    protected abstract List<S> selectOldListByParam(MigrateJobParam jobParam);

    /**
     * 获取源数据已存在的映射数据的ID，存在映射的数据，就对数据更新，不存在就是新增(机构，use的数据从医院的OA系统同步，1期没有接入医院OA系统，所以数据要持续同步，滚动更新)
     *
     * @param bo
     * @return 如果存在映射的新数据，返回新数据，否则返回null
     */
    protected Object selectExistsMappingDataId(MigrationBaseBO bo) {
        Object oldId = getOldId((S) bo.getSource());
        DsCommonMapping dsCommonMapping = dsCommonMappingService.selectMappingBySourceValue(bo.getModuleEnum(), oldId);
        if (Objects.isNull(dsCommonMapping) || StringUtils.isBlank(dsCommonMapping.getTargetValue())) {
            return null;
        }
        return bo.getSelectNewTargetFun().apply(Long.valueOf(dsCommonMapping.getTargetValue()));
    }


    public List<AfterProcessField> migrateData(MigrationBaseBO bo) {
        log.info("=======migrateData源数据={}", JSON.toJSONString(bo.getSource()));
        //记录源数据到mongo
        log.info("=======记录源数据到mongo={}", JSON.toJSONString(bo.getSource()));
        this.logOldDataToMongo((S) bo.getSource(), bo.getModuleEnum().getRecordEnum());
        Long s = System.currentTimeMillis();
        Long start = System.currentTimeMillis();
        createBaseTarget(bo);
        if (ObjectUtils.isEmpty(bo.getTarget())) {
            log.warn("经过createBaseTarget后Target为空,直接跳出");
            return new ArrayList<>();
        }
        log.info("=======createBaseTarget耗时=[{}]", System.currentTimeMillis() - s);
        s = System.currentTimeMillis();
        this.dealExtend(bo);
        log.info("=======dealExtend耗时=[{}]", System.currentTimeMillis() - s);
        s = System.currentTimeMillis();
        this.remainValues(bo);
        log.info("=======remainValues耗时=[{}]", System.currentTimeMillis() - s);
        s = System.currentTimeMillis();

        bo.setTenantIdToTargetData();
        bo.setSequenceId();
        bo.setTargetIdToOtherData();
        transactionTemplate.execute(status -> {
            try {
                saveTargetData(bo);
            } catch (Exception e) {
                log.error("saveTargetData异常Exception{}", e);
                status.setRollbackOnly();
                throw e;
            }
            return Boolean.TRUE;
        });
        log.info("=======saveTargetData耗时=[{}]", System.currentTimeMillis() - s);
        log.info("*******migrateData总耗时=[{}]", System.currentTimeMillis() - start);
        //同步数据之后发送mq消息更新对应的redis缓存
        this.refreshCache(bo);
        this.doOtherThing(bo);
        return bo.getAfterProcessFieldList();
    }

    /**
     * 整体处理后做点私活，dirtyCode
     *
     * @param bo
     */
    protected void doOtherThing(MigrationBaseBO bo) {

    }

    /**
     * 可根据实际情况重写该方法，不重写即使用默认的
     *
     * @param bo
     */
    protected void refreshCache(MigrationBaseBO bo) {
        DsCommonMappingEnum moduleEnum = bo.getModuleEnum();
        if (Objects.nonNull(moduleEnum.getCacheRefreshEnum())) {

        }
    }


    /**
     * 记录2.0的数据到mongo
     *
     * @param data
     * @param recordEnum
     */
    private void logOldDataToMongo(S data, DataSyncRecordEnum recordEnum) {
        if (Objects.isNull(recordEnum)) {
            return;
        }
        RecordOldDataDTO oldDataDTO = new RecordOldDataDTO();
        oldDataDTO.setTenantId(TenantUtils.getMappingTenantId());
        oldDataDTO.setCode(recordEnum.getCode());
        oldDataDTO.setName(recordEnum.getName());
        oldDataDTO.setDesc(recordEnum.getDesc());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        oldDataDTO.setOperateTime(now.format(formatter));
        oldDataDTO.setCreateTime(now.format(formatter));
        List<OldDataDTO> dataDTOS = new LinkedList<>();
        OldDataDTO recordData = new OldDataDTO();
        recordData.setBefore(JSON.toJSONString(data));
        recordData.setBusinessId(String.valueOf(getOldId(data)));
        recordData.setChangeType(DataChangeType.ADD.name());
        dataDTOS.add(recordData);
        oldDataDTO.setDataDTOS(dataDTOS);
        recordMongoDao.saveOperateData(oldDataDTO);

    }

    /**
     * 根据参数查询数据
     *
     * @param jobParam
     * @return
     */
    @Override
    public List<S> selectSourceData(MigrateJobParam jobParam) {
        return selectOldListByParam(jobParam);
    }

    /**
     * 按需重写该方法
     *
     * @param bo
     */
    @Override
    public void dealExtend(MigrationBaseBO bo) {

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

}
