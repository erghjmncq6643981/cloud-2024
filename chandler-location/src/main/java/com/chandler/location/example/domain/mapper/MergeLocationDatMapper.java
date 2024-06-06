package com.chandler.location.example.domain.mapper;

import com.chandler.location.example.domain.dataobject.MergeLocationData;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface MergeLocationDatMapper extends BaseMapper<MergeLocationData> {
}