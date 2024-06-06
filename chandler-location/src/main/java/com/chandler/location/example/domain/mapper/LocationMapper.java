package com.chandler.location.example.domain.mapper;

import com.chandler.location.example.domain.dataobject.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * 类功能描述
 *
 * @version 1.0.0
 * @author 钱丁君-chandler 2024/3/18 14:08
 * @since 1.8
 */
@Mapper
public interface LocationMapper extends BaseMapper<Location> {

    Location selectFirst(Long taskId);
    Location selectEnd(Long taskId);
    int batchSave(@Param("locations") List<Location> locations);

    List<Location> selectTarget(@Param("inPolygon") Integer inPolygon);

    List<Location> selectALL(@Param("size") Integer size);
}