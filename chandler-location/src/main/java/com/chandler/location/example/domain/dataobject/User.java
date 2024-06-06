/*
 * chandler-instance-client
 * 2024/3/4 17:26
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.domain.dataobject;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/4 17:26
 * @since 1.8
 */
@Data
@Table(name="user")
public class User {
    @Id
    Long id;
    String name;
    Integer age;
    String password;
}