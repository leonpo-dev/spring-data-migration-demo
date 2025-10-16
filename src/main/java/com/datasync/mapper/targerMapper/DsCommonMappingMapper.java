package com.datasync.mapper.targerMapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datasync.entity.targetDTO.DsCommonMapping;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通用映射关系表 Mapper 接口
 * </p>
 *
 * @author system
 * @since 2023-05-16
 */
@Mapper
@DS("targetDB")
public interface DsCommonMappingMapper extends BaseMapper<DsCommonMapping> {

}
