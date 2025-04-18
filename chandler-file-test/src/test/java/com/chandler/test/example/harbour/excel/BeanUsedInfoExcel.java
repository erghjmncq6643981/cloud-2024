/*
 * chandler-file-test
 * 2024/12/5 10:50
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.harbour.excel;

import lombok.Data;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/5 10:50
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class BeanUsedInfoExcel {
    String name;
    String className;
    Integer usedTimes;
    List<String> parents;
    String topParent;

}