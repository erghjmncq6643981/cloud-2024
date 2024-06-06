package com.chandler.location.example.domain.mapper;

import com.chandler.location.example.domain.dataobject.OriginalLocationData;
import com.chandler.location.example.entity.OriginalLocationParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Mapper
public interface OriginalLocationDataMapper extends BaseMapper<OriginalLocationData> {

    List<OriginalLocationData> selectData(OriginalLocationParam param);

    int batchSave(List<OriginalLocationData> points);
}