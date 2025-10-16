package com.datasync.job;

import com.alibaba.fastjson2.JSON;
import com.datasync.common.DsCommonMappingEnum;
import com.datasync.common.MigrateJobParam;
import com.datasync.common.SyncStatusEnum;
import com.datasync.entity.targetDTO.DsIgnoreModule;
import com.datasync.entity.targetDTO.DsSyncLog;
import com.datasync.service.syncdata.DsIgnoreModuleService;
import com.datasync.service.syncdata.JobSyncLogService;
import com.datasync.service.syncdata.MigrateManger;
import com.datasync.util.TenantUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author guanyuanfei
 * @date 2023/5/17 19:26
 */
@Slf4j
@Component
public class MigrationDataJob {

    @Resource
    private MigrateManger migrateManger;

    @Resource
    private JobSyncLogService jobSyncLogService;

    @Resource
    private DsIgnoreModuleService dsIgnoreModuleService;

    @XxlJob("migrationDataJob")
    public ReturnT<String> doJob() throws Exception {
        String param = XxlJobHelper.getJobParam();
        log.info("param={}", param);
        if (StringUtils.isBlank(param)) {
            log.warn("param=空不执行了！！！！");
            return ReturnT.SUCCESS;
        }
        MigrateJobParam jobParam = JSON.parseObject(param, MigrateJobParam.class);
        if (Objects.isNull(jobParam.getSourceTenantCode())) {
            log.warn("参数中的租户信息为空，不能继续处理job结束");
            return ReturnT.SUCCESS;
        }
        if (StringUtils.isBlank(jobParam.getModuleCode())) {
            //按默认的模块顺序迁移
            List<String> sortModuleCodeList = Arrays.stream(DsCommonMappingEnum.values()).sorted(Comparator.comparing(DsCommonMappingEnum::getOrder)).map(DsCommonMappingEnum::getModuleCode).collect(Collectors.toList());
            List<String> ignoreModules = selectIgnoreModules();
            log.warn("忽略的模块ignoreModules={}", ignoreModules);
            for (String moduleCode : sortModuleCodeList) {
                if (ignoreModules.contains(moduleCode)) {
                    log.warn("按默认的模块顺序迁移时moduleCode=[{}]在忽略列表中，该模块不再迁移", moduleCode);
                    continue;
                }
                jobParam.setModuleCode(moduleCode);
                doMigrate(jobParam);
            }
        } else {
            log.info("按job参数进行迁移");
            doMigrate(jobParam);
        }
        TenantUtils.remove();
        return ReturnT.SUCCESS;
    }

    public Boolean doMigrate(MigrateJobParam jobParam) {
        TenantUtils.getMappingTenantId(jobParam.getSourceTenantCode());
        getDefaultTimeParam(jobParam);
        //根据参数 记录 迁移log
        DsSyncLog dsSyncLog = jobSyncLogService.getSyncLog(jobParam);
        if (Objects.isNull(dsSyncLog)) {
            log.warn("根据参数生成迁移记录是返回 null job结束");
            return Boolean.TRUE;
        }
        try {
            //更新 进行中 状态
            dsSyncLog.setSyncStatus(SyncStatusEnum.IN_PROGRESS.getCode());
            jobSyncLogService.updateSyncLog(dsSyncLog);
            Boolean result = migrateManger.migrate(jobParam);
            if (result) {
                dsSyncLog.setSyncStatus(SyncStatusEnum.SUCCEED.getCode());
                dsSyncLog.setExceptionMsg("");
            } else {
                dsSyncLog.setSyncStatus(SyncStatusEnum.FAIL.getCode());
            }
        } catch (Exception e) {
            log.error("迁移数据异常e{}", e);
            dsSyncLog.setSyncStatus(SyncStatusEnum.FAIL.getCode());
            dsSyncLog.setExceptionMsg(e.getMessage());
        }
        //更新记录
        jobSyncLogService.updateSyncLog(dsSyncLog);
        return Boolean.TRUE;
    }


    private void getDefaultTimeParam(MigrateJobParam param) {
        if (StringUtils.isNotBlank(param.getStartTime()) || (StringUtils.isNotBlank(param.getEndTime()))) {
            return;
        }

        LocalDate startDate = LocalDate.now().plusDays(-1);
        LocalDate endDate = LocalDate.now();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        param.setStartTime(startDate.format(fmt));
        param.setEndTime(endDate.format(fmt));

    }


    private List<String> selectIgnoreModules() {
        return dsIgnoreModuleService.lambdaQuery().list()
                .stream()
                .map(DsIgnoreModule::getModuleCode)
                .collect(Collectors.toList());
    }


}
