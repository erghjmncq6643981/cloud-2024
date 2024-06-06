package com.chandler.location.example.domain.mapper;

import com.chandler.location.example.domain.dataobject.Task;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * 类功能描述
 *
 * @version 1.0.0  
 * @author 钱丁君-chandler 2024/3/18 14:34
 * @since 1.8
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    Task selectTask();
    List<Task> selectParseTasks();
    Task selectExsit(Task t);
   List<Task> selectTasks(Task t);
}