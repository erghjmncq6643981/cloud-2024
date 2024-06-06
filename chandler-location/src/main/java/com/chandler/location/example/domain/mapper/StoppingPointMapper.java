package com.chandler.location.example.domain.mapper;

import com.chandler.location.example.domain.dataobject.StoppingPoint;
import com.chandler.location.example.domain.dataobject.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Mapper
public interface StoppingPointMapper extends BaseMapper<StoppingPoint> {
    List<StoppingPoint> selectPoints(@Param("taskId")Long taskId);
    int batchSave(List<StoppingPoint> points);
}