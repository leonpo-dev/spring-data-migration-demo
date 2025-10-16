package com.datasync.service.syncdata;

import com.alibaba.fastjson2.JSON;
import com.datasync.common.*;
import com.datasync.config.SpringBeans;
import com.datasync.entity.targetDTO.DsAfterProcess;
import com.datasync.service.bo.syncdata.AfterProcessField;
import com.datasync.service.bo.syncdata.MigrationBaseBO;
import com.datasync.util.Sequence;
import com.datasync.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Leonpo
 * @date 2023/5/18 11:52
 */
@Slf4j
@Component
public class MigrateManger<S> {


    @Resource
    private DsAfterProcessService dsAfterProcessService;


    /**
     * 数据迁移
     *
     * @param jobParam
     * @return
     */
    public Boolean migrate(MigrateJobParam jobParam) {


        DsCommonMappingEnum moduleEnum = DsCommonMappingEnum.matchByModuleCode(jobParam.getModuleCode());
        if (Objects.isNull(moduleEnum)) {
            log.warn("数据迁移时moduleCode=[{}]没匹配到定义的枚举，直接返回", moduleEnum);
            return Boolean.TRUE;
        }
        Class<? extends DataProcessService> processServiceClass = moduleEnum.getProcessService();
        if (Objects.nonNull(processServiceClass)) {
            DataProcessService service = SpringBeans.getBean(processServiceClass);
            // 老的租户和新的租户ID的映射
            List<S> sourceData = service.selectSourceData(jobParam);
            if (CollectionUtils.isNotEmpty(sourceData)) {
                List<AfterProcessField> afterProcessFieldList = new LinkedList<>();
                for (S data : sourceData) {
                    MigrationBaseBO bo = service.getBO(data);
                    afterProcessFieldList.addAll(service.migrateData(bo));
                }
                if (CollectionUtils.isNotEmpty(afterProcessFieldList)) {
                    log.info("有后置处理的数据afterProcessFieldList.size=[{}]", afterProcessFieldList.size());
                    List<AfterProcessField> afterProcessResult = applyAfterProcess(afterProcessFieldList, NumConstants.NUM_0);
                    if (CollectionUtils.isNotEmpty(afterProcessResult)) {
                        log.warn("后置处理的数据重试后仍有数据没有完成映射size=[{}],记录到表中", afterProcessResult.size());
                        this.saveAfterProcessData(afterProcessResult);
                    }
                }
            } else {
                log.warn("数据迁移时查询到需迁移的数据为空，直接返回");
            }
        } else {
            log.warn("数据迁移时moduleCode=[{}]实现服务为null，直接返回", moduleEnum);
        }
        //迁移完后处理的事情
        this.afterMigrate(jobParam, moduleEnum);
        return Boolean.TRUE;

    }

    /**
     * 模块的数据迁移完后,后续处理的事情
     * 已知的有：
     * 1.查询忽略不表数据，一些场景的数据不需要再次迁移
     * 2.失效 redis中 子系统列表的缓存 key:BUSINESS_SYSTEM_LIST
     * 3.机构在redis中的缓存整个失效 key:ORGANIZATION_TENANT_ID ORGANIZATION_HASH_KEY
     *
     * @param jobParam
     * @param moduleEnum
     */
    private void afterMigrate(MigrateJobParam jobParam, DsCommonMappingEnum moduleEnum) {
    }


    private void saveAfterProcessData(List<AfterProcessField> afterProcessResult) {
        List<DsAfterProcess> processList = afterProcessResult.stream().map(e -> {
            DsAfterProcess dsAfterProcess = new DsAfterProcess();
            dsAfterProcess.setId(Sequence.getSEQ());
            dsAfterProcess.setTheData(JSON.toJSONString(e.getTarget()));
            dsAfterProcess.setTenantId(TenantUtils.getMappingTenantId());
            return dsAfterProcess;
        }).collect(Collectors.toList());

        dsAfterProcessService.saveBatch(processList);
        throw new BusinessRuntimeException(ErrorCode.THERE_ARE_ALSO_OUTSTANDING_RECORDS);
    }

    /**
     * 尝试10次后结束
     *
     * @param afterProcessList
     * @param count
     * @return
     */
    private List<AfterProcessField> applyAfterProcess(List<AfterProcessField> afterProcessList, int count) {
        if (CollectionUtils.isEmpty(afterProcessList) || count > NumConstants.NUM_5 * NumConstants.NUM_2) {
            return afterProcessList;
        }
        List<AfterProcessField> processFields = afterProcessList.stream()
                .filter(e -> !(boolean) e.getAfterProcessUpdateService().apply(e.getTarget()))
                .collect(Collectors.toList());
        return applyAfterProcess(processFields, count + 1);
    }
}
