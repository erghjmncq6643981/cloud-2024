/*
 * hr-data-sync-backend
 * 2024/11/11 15:37
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

/**
 * 类功能描述
 *
 * @version 1.0.0
 * @author 钱丁君-chandler 2024/11/11 15:37
 * @since 1.8
 */
@Data
public class JarAnalyzeExcel {
    Integer 序号;
    String JAR名称;
    Integer 层级;
    String 父JAR;
    Boolean 是否有子JAR;
    String JAR类型;
    String groupId;
    String 版本;
    String scope;
}
