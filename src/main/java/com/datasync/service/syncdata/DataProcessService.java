package com.datasync.service.syncdata;


import com.datasync.common.MigrateJobParam;
import com.datasync.service.bo.syncdata.AfterProcessField;
import com.datasync.service.bo.syncdata.MigrationBaseBO;

import java.util.List;

/**
 * @author Leonpo
 * @date 2023/5/16 10:00
 */
public interface DataProcessService<S> {


    MigrationBaseBO getBO(S source);

    /**
     * 根据参数查询数据
     *
     * @param jobParam
     * @return
     */
    List<S> selectSourceData(MigrateJobParam jobParam);

    /**
     * 迁移数据总流程
     *
     * @param bo
     * @return
     */
    List<AfterProcessField> migrateData(MigrationBaseBO bo);

    /**
     * 生成对应的基础数据
     *
     * @param bo
     */
    void createBaseTarget(MigrationBaseBO bo);

    /**
     * 处理数据的扩展表数据
     *
     * @param bo
     */
    void dealExtend(MigrationBaseBO bo);

    /**
     * 记录一下2.0中剩余未迁移的字段，统一记录到一个临时表
     *
     * @param bo
     */
    void remainValues(MigrationBaseBO bo);

    /**
     * 保存目标数据
     *
     * @param bo
     */
    void saveTargetData(MigrationBaseBO bo);

}
