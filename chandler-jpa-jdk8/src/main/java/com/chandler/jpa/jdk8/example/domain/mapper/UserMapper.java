/*
 * chandler-instance-client
 * 2024/3/4 17:29
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.jpa.jdk8.example.domain.mapper;

import com.chandler.jpa.jdk8.example.domain.dataobject.User;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/4 17:29
 * @since 1.8
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
