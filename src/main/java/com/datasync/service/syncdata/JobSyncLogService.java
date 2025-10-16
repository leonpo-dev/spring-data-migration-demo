package com.datasync.service.syncdata;


import com.datasync.common.MigrateJobParam;
import com.datasync.entity.targetDTO.DsSyncLog;

/**
 * @author Leonpo
 * @date 2023/5/18 10:23
 */
public interface JobSyncLogService {

    DsSyncLog getSyncLog(MigrateJobParam defaultTimeParam);

    void updateSyncLog(DsSyncLog dsSyncLog);
}
