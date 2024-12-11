/*
 * chandler-file-test
 * 2024/12/5 10:42
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.harbour.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/5 10:42
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class BeanInfoBO {
    Integer id;
    Integer parentId;
    String name;
    Long startMillis;
    Long endMillis;
    Long duration;
    Long actualDuration;
    Map<String,String> tags;
    List<BeanInfoBO> children;
}